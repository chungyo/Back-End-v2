package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Planner;
import com.mmos.mmos.src.repository.CalendarRepository;
import com.mmos.mmos.src.repository.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
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
        Planner planner = new Planner(today, calendar);

        return plannerRepository.save(planner);
    }
}
