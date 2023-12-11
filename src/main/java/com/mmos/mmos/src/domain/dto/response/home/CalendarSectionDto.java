package com.mmos.mmos.src.domain.dto.response.home;

import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Plan;
import com.mmos.mmos.src.domain.entity.Planner;
import com.mmos.mmos.src.domain.entity.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CalendarSectionDto {
    private Long idx;
    private Integer year;
    private Integer monthValue;
    private String month;
    private List<Project> projects;
    private List<Integer> planDays = new ArrayList<>();

    public CalendarSectionDto(Calendar calendar, List<Project> projects) {
        this.idx = calendar.getCalendarIndex();
        this.year = calendar.getCalendarYear();
        this.monthValue = calendar.getCalendarMonth();
        this.month = Month.of(monthValue).toString();
        this.projects = projects;

        for (Planner planner : calendar.getCalendarPlanners()) {
            if(!planner.getPlannerPlans().isEmpty())
                planDays.add(planner.getPlannerDate().getDayOfMonth());
        }
    }
}
