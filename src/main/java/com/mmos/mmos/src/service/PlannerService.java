package com.mmos.mmos.src.service;

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

    public Calendar findCalendar(Long calendarIdx) {
        return calendarRepository.findById(calendarIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캘린더입니다. CALENDAR_INDEX=" + calendarIdx));
    }

    // 플래너 생성
    @Transactional
    public Planner savePlanner(LocalDate today, Long calendarIdx) {
        Calendar calendar = findCalendar(calendarIdx);

        // 막 회원 가입을 한 유저가 아니면서 같은 날의 플래너가 이미 존재할 때 생성 막기
        if(!calendar.getCalendar_planners().isEmpty() && calendar.getCalendar_planners().get(calendar.getCalendar_planners().size() - 1).getPlanner_date().equals(today))
            return null;

        Planner planner = new Planner(today, calendar);
        // Calendar, Planner 양방향 매핑
        calendar.addPlanner(planner);

        return plannerRepository.save(planner);
    }

}
