package com.mmos.mmos.src.domain.dto.calendar;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Plan;
import com.mmos.mmos.src.domain.entity.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CalendarResponseDto {
    private Long idx;
    private int month;
    private Long monthlyStudyTime;
    private Long monthlyCompletedPlanNum;
    private HttpResponseStatus status = null;
    private List<Project> projects;
    private List<Plan> plans;

    public CalendarResponseDto(Calendar calendar, HttpResponseStatus status, List<Project> projects, List<Plan> plans) {
        this.idx = calendar.getCalendarIndex();
        this.month = calendar.getCalendarMonth();
        this.monthlyStudyTime = calendar.getCalendarMonthlyStudyTime();
        this.monthlyCompletedPlanNum = calendar.getCalendarMonthlyCompletedPlanNum();
        this.projects = projects;
        this.status = status;
        this.plans = plans;
    }
}
