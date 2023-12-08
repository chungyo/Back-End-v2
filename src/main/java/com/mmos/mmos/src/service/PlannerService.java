package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.DuplicateRequestException;
import com.mmos.mmos.config.exception.EmptyEntityException;
import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Planner;
import com.mmos.mmos.src.repository.PlannerRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@Service
public class PlannerService {

    private final PlannerRepository plannerRepository;
    private final CalendarService calendarService;

    public PlannerService(PlannerRepository plannerRepository, @Lazy CalendarService calendarService) {
        this.plannerRepository = plannerRepository;
        this.calendarService = calendarService;
    }

    public Planner findPlannerByCalendarIdxAndDay(Long calendarIdx, LocalDate day) throws BaseException {
        return plannerRepository.findPlannerByCalendar_CalendarIndexAndPlannerDate(calendarIdx, day)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_PLANNER));
    }

    public Planner findPlannerByIdx(Long plannerIdx) throws BaseException {
        return plannerRepository.findById(plannerIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_PLANNER));
    }

    // 플래너 생성
    @Transactional
    public Planner savePlanner(Long calendarIdx, LocalDate today) throws BaseException {
        try {
            Calendar calendar = calendarService.findCalendarByIdx(calendarIdx);

            // 막 회원 가입을 한 유저가 아니면서 같은 날의 플래너가 이미 존재할 때 생성 막기
            if(plannerRepository.findPlannerByCalendar_CalendarIndexAndPlannerDate(calendarIdx, today).isPresent()) {
                throw new DuplicateRequestException(DUPLICATE_PLANNER);
            }

            Planner planner = new Planner(today, calendar);
            // Calendar, Planner 양방향 매핑
            calendar.addPlanner(planner);

            return plannerRepository.save(planner);
        } catch (EmptyEntityException |
                 DuplicateRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Planner getPlannerByCalendarAndDate(Long calendarIdx, LocalDate day) throws BaseException {
        try {
            return findPlannerByCalendarIdxAndDay(calendarIdx, day);
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
