package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.plan.PlanNameUpdateRequestDto;
import com.mmos.mmos.src.domain.dto.plan.PlanResponseDto;
import com.mmos.mmos.src.domain.dto.plan.PlanSaveRequestDto;
import com.mmos.mmos.src.domain.entity.*;
import com.mmos.mmos.src.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlannerRepository plannerRepository;
    private final UserStudyRepository userStudyRepository;
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    private final PlannerService plannerService;
    private final CalendarService calendarService;

    public Planner findPlannerByIdx(Long plannerIdx) {
        return plannerRepository.findById(plannerIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 플래너입니다. PLANNER_INDEX=" + plannerIdx));
    }

    public UserStudy findUserStudyByIdx(Long userStudyIdx) {
        return userStudyRepository.findById(userStudyIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자가 가입된 스터디입니다. USERSTUDY_INDEX=" + userStudyIdx));
    }

    public Plan findPlanByIdx(Long planIdx) {
        return planRepository.findById(planIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 플랜입니다. PLAN_INDEX=" + planIdx));
    }

    public List<Plan> findPlansByIsStudy(Boolean isStudy) {
        return planRepository.findPlansByPlanIsStudy(isStudy)
                .orElseThrow(() -> new IllegalArgumentException("스터디 계획 == true, 일반 계획 == false 다시 검색해 주세요. 현재 입력 =  " + isStudy));
    }

    public Planner findPlannerByCalendarIdxAndDate(Long calendarIdx, LocalDate date) {
        return plannerRepository.findPlannerByCalendar_CalendarIndexAndPlannerDate(calendarIdx, date)
                .orElse(null);
    }

    public User findUserByIdx(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. USER_INDEX=" + userIdx));
    }

    public Calendar findCalendarByUserIdx(Long userIdx, int month) {
        return calendarRepository.findCalendarByUser_UserIndexAndCalendarMonth(userIdx, month)
                .orElse(null);
    }

    public List<Plan> findPlans() {
        return planRepository.findAll();
    }

    @Transactional
    public PlanResponseDto savePlan(PlanSaveRequestDto requestDto, Long userIdx) {
        try {
            // 저장하려는 월의 캘린더가 존재하는지 확인
            // 존재한다면 DB에서 가져오고, 존재하지 않는다면 DB에 새로 저장 후, 저장한 값 가져오기
            Long calendarIdx = findCalendarByUserIdx(userIdx, requestDto.getDate().getMonthValue()).getCalendarIndex();
            if(calendarIdx == null) {
                calendarIdx = calendarService.saveCalendar(requestDto.getDate().getMonthValue(), userIdx).getIdx();
            }
            // 저장하려는 날의 플래너가 존재하는지 확인
            // 존재한다면 DB에서 가져오고, 존재하지 않는다면 DB에 새로 저장 후, 저장한 값 가져오기
            Long plannerIdx = findPlannerByCalendarIdxAndDate(calendarIdx, LocalDate.now()).getPlannerIndex();
            if(plannerIdx == null)
                plannerIdx = plannerService.savePlanner(requestDto.getDate(), calendarIdx).getIdx();

            UserStudy userStudy = null;
            if (requestDto.getIsStudy()) {
                userStudy = findUserStudyByIdx(requestDto.getUserStudyIdx());
            }

            // Plan 객체 생성
            Planner planner = findPlannerByIdx(plannerIdx);
            Plan plan = new Plan(requestDto, planner, userStudy);

            // 역 FK 매핑
            planner.addPlan(plan);

            return new PlanResponseDto(planRepository.save(plan));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    public PlanResponseDto getPlan(Long planIdx) {
        Plan plan = findPlanByIdx(planIdx);

        return new PlanResponseDto(plan);
    }

    // 스터디에 포함된 계획 = true
    // 스터디에 포함되지 않은 계획 = false
    @Transactional
    public List<PlanResponseDto> getPlansByPlanIsStudy(Boolean isStudy) {
        List<Plan> planList = findPlansByIsStudy(isStudy);
        List<PlanResponseDto> responseDtoList = new ArrayList<>();

        for (Plan plan : planList) {
            responseDtoList.add(new PlanResponseDto(plan));
        }

        return responseDtoList;
    }

    @Transactional
    public List<PlanResponseDto> getPlans() {
        List<PlanResponseDto> responseDtoList = new ArrayList<>();
        List<Plan> plans = findPlans();

        for (Plan plan : plans) {
            responseDtoList.add(new PlanResponseDto(plan));
        }

        return responseDtoList;
    }

    @Transactional
    public PlanResponseDto updatePlan(Long planIdx, PlanNameUpdateRequestDto requestDto) {
        Plan plan = findPlanByIdx(planIdx);

        plan.update(requestDto.getPlanName());

        return new PlanResponseDto(plan);
    }
}
