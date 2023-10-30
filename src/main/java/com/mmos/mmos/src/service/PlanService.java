package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.BusinessLogicException;
import com.mmos.mmos.config.exception.EmptyEntityException;
import com.mmos.mmos.config.exception.OutOfRangeException;
import com.mmos.mmos.src.domain.dto.request.PlanSaveRequestDto;
import com.mmos.mmos.src.domain.dto.request.PlanUpdateRequestDto;
import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Plan;
import com.mmos.mmos.src.domain.entity.Planner;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.PlanRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@Service
public class PlanService {

    private final PlanRepository planRepository;
    private final PlannerService plannerService;
    private final ProjectService projectService;

    public PlanService(PlanRepository planRepository, @Lazy PlannerService plannerService, @Lazy ProjectService projectService) {
        this.planRepository = planRepository;
        this.plannerService = plannerService;
        this.projectService = projectService;
    }

    public Plan findPlanByIdx(Long planIdx) throws BaseException {
        return planRepository.findById(planIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_PLAN));
    }

    public List<Plan> findPlansIsVisible(Calendar calendar) throws BaseException {
        return planRepository.findPlansByPlanIsVisibleIsTrueAndPlanner_Calendar(calendar)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_PLAN));
    }

    @Transactional
    public Plan savePlan(Long plannerIdx, PlanSaveRequestDto requestDto) throws BaseException{
        try {
            Planner planner = plannerService.findPlannerByIdx(plannerIdx);
            Plan plan = new Plan(requestDto, planner);
            planner.addPlan(plan);
            planner.getCalendar().getUser().updateTotalSchedule(true);

            return planRepository.save(plan);
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        }
    }

    @Transactional
    public Plan getPlan(Long planIdx) throws BaseException {
        try {
            return findPlanByIdx(planIdx);
        } catch (EmptyEntityException e) {
            throw new BaseException(EMPTY_PLAN);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // plan 완수 여부 기능
    @Transactional
    public Plan updatePlanIsComplete(Long planIdx) throws BaseException {
        try {
            // 객체 가져오기
            Plan plan = findPlanByIdx(planIdx);
            Planner planner = plan.getPlanner();
            Calendar calendar = planner.getCalendar();
            User user = planner.getCalendar().getUser();

            // 업데이트
            if(plan.getPlanIsComplete()) {
                plan.updateIsComplete(false);

                planner.updateDailyScheduleNum(false);
                calendar.updateMonthlyPlanNum(false);
                user.updateTotalCompleteSchedule(false);
            } else {
                plan.updateIsComplete(true);

                planner.updateDailyScheduleNum(true);
                calendar.updateMonthlyPlanNum(true);
                user.updateTotalCompleteSchedule(true);
            }

            return plan;
        } catch (EmptyEntityException e) {
            throw new BaseException(EMPTY_PLAN);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Plan updatePlan(Long planIdx, PlanUpdateRequestDto requestDto) throws BaseException {
        try {
            Plan plan = findPlanByIdx(planIdx);
            plan.updateName(requestDto.getNewName());

            return plan;
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Plan updatePlanIsVisible(Long planIdx) throws BaseException {
        try {
            Plan plan = findPlanByIdx(planIdx);
            Planner planner = plan.getPlanner();
            User user = plan.getPlanner().getCalendar().getUser();

            Long visiblePlanCount = planRepository.countByPlannerAndPlanIsVisibleTrue(planner);
            Long visibleProjectCount = projectService.countByUserAndProjectIsVisibleTrue(user);

            if (!plan.getPlanIsVisible()) {
                if (visiblePlanCount + visibleProjectCount >= 5)
                    throw new OutOfRangeException(PLAN_ISVISIBLE_FULL);
                plan.updateIsVisible(true);
            } else {
                plan.updateIsVisible(false);
            }

            return plan;
        } catch (EmptyEntityException |
                 OutOfRangeException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Plan setStudyTime(Long planIdx) throws BaseException {
        try {
            Plan plan = findPlanByIdx(planIdx);
            if(!plan.getPlanner().getPlannerDate().isEqual(LocalDate.now()))
                throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);

            Planner planner = plan.getPlanner();
            Calendar calendar = planner.getCalendar();
            User user = calendar.getUser();

            if(plan.getStudytimeStartTime() == null) {
                // 계획 시작 시간 설정
                plan.setStartTime(new Timestamp(System.currentTimeMillis()));
            } else {
                Timestamp now = new Timestamp(System.currentTimeMillis());
                // 시간 차이 계산 (분 단위)
                Long studyTime = (now.getTime() - plan.getStudytimeStartTime().getTime()) / 60000;

                // 총 공부 시간 계산
                plan.setStudyTime(studyTime);
                planner.addTime(studyTime);
                calendar.addTime(studyTime);
                user.addTotalTime(studyTime);
                user.addWeeklyTime(studyTime);

                // 시간 초기화
                plan.resetTime();
            }

            return plan;
        } catch (EmptyEntityException |
                BusinessLogicException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Plan deletePlan(Long planIdx) throws BaseException {
        try {
            Plan plan = findPlanByIdx(planIdx);
            Planner planner = plan.getPlanner();
            Calendar calendar = planner.getCalendar();
            User user = calendar.getUser();

            planner.minusTime(plan.getPlanStudyTime());
            calendar.minusTime(plan.getPlanStudyTime());
            user.minusTotalTime(plan.getPlanStudyTime());

            if(plan.getPlanIsComplete())
                user.updateTotalCompleteSchedule(false);
            user.updateTotalSchedule(false);



            LocalDate today = LocalDate.now();
            LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);
            LocalDate startOfWeek = today.with(DayOfWeek.MONDAY); // 이번 주의 월요일로 설정

            System.out.println("startOfWeek = " + startOfWeek);
            System.out.println("endOfWeek = " + endOfWeek);

            // 이번 주 계획인지 확인하고 맞다면 이번 주 공부 시간에서도 삭제
            for(; today.isBefore(endOfWeek) || today.isEqual(endOfWeek); today = today.plusDays(1)) {
                if(today.isEqual(plan.getPlanner().getPlannerDate())) {
                    System.out.println(today.getDayOfMonth());
                    user.minusWeeklyTime(plan.getPlanStudyTime());
                    break;
                }
            }

            planRepository.delete(plan);

            return plan;
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
