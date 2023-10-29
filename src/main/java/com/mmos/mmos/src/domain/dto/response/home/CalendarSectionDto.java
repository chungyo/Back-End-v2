package com.mmos.mmos.src.domain.dto.response.home;

import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Plan;
import com.mmos.mmos.src.domain.entity.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Month;
import java.util.List;

@Getter
@NoArgsConstructor
public class CalendarSectionDto {
    private Long idx;
    private Integer year;
    private Integer monthValue;
    private String month;
    private List<Project> projects;
    private List<Plan> plans;

    public CalendarSectionDto(Calendar calendar, List<Project> projects, List<Plan> plans) {
        this.idx = calendar.getCalendarIndex();
        this.year = calendar.getCalendarYear();
        this.monthValue = calendar.getCalendarMonth();
        this.month = Month.of(monthValue).toString();
        this.projects = projects;
        this.plans = plans;
    }
}