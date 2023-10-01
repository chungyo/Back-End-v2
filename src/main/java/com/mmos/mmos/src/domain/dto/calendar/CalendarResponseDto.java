package com.mmos.mmos.src.domain.dto.calendar;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.src.domain.dto.plan.PlanResponseDto;
import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CalendarResponseDto {
    private Long idx;
    private Integer year;
    private Integer month;
    private Long monthlyStudyTime;
    private Long monthlyCompletedPlanNum;
    private HttpResponseStatus status = null;
    private List<Project> projects;
    private List<PlanResponseDto> plans;

    public CalendarResponseDto(Calendar calendar, HttpResponseStatus status, List<Project> projects, List<PlanResponseDto> plans) {
        this.idx = calendar.getCalendarIndex();
        this.year = calendar.getCalendarYear();
        this.month = calendar.getCalendarMonth();
        this.monthlyStudyTime = calendar.getCalendarMonthlyStudyTime();
        this.monthlyCompletedPlanNum = calendar.getCalendarMonthlyCompletedPlanNum();
        this.projects = projects;
        this.status = status;
        this.plans = plans;
    }

    public CalendarResponseDto(HttpResponseStatus status) {
        this.idx = null;
        this.year = null;
        this.month = null;
        this.monthlyStudyTime = null;
        this.monthlyCompletedPlanNum = null;
        this.projects = null;
        this.status = status;
        this.plans = null;
    }
}
