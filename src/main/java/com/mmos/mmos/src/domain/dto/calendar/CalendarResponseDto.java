package com.mmos.mmos.src.domain.dto.calendar;

import com.mmos.mmos.src.domain.entity.Calendar;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CalendarResponseDto {
    private Long idx;
    private int month;
    private Long monthlyStudyTime;
    private Long monthlyCompletedPlanNum;

    public CalendarResponseDto(Calendar calendar) {
        this.idx = calendar.getCalendarIndex();
        this.month = calendar.getCalendarMonth();
        this.monthlyStudyTime = calendar.getCalendarMonthlyStudyTime();
        this.monthlyCompletedPlanNum = calendar.getCalendarMonthlyCompletedPlanNum();
    }
}
