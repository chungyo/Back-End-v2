package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.planner.PlannerResponseDto;
import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Planner;
import com.mmos.mmos.src.repository.CalendarRepository;
import com.mmos.mmos.src.repository.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PlannerRepository plannerRepository;
    private final CalendarRepository calendarRepository;

    public Calendar findCalendarByIdx(Long calendarIdx) {
        return calendarRepository.findById(calendarIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캘린더입니다. CALENDAR_INDEX=" + calendarIdx));
    }

    // 플래너 생성
    @Transactional
    public PlannerResponseDto savePlanner(LocalDate today, Long calendarIdx) {
        Calendar calendar = findCalendarByIdx(calendarIdx);

        // 막 회원 가입을 한 유저가 아니면서 같은 날의 플래너가 이미 존재할 때 생성 막기
        if(!calendar.getCalendarPlanners().isEmpty() && calendar.getCalendarPlanners().get(calendar.getCalendarPlanners().size() - 1).getPlannerDate().equals(today))
            return null;

        Planner planner = new Planner(today, calendar);
        // Calendar, Planner 양방향 매핑
        calendar.addPlanner(planner);

        plannerRepository.save(planner);

        return new PlannerResponseDto(planner);
    }

}
