package com.mmos.mmos.src.domain.dto.planner;

import com.mmos.mmos.src.domain.entity.Planner;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PlannerResponseDto {

    private Long idx;
    private LocalDate date;
    private String memo;
    private Long dailyStudyTime;
    private Long dailyScheduleNum;
    private Boolean isPublic;
    private Long dday;

    public PlannerResponseDto(Planner planner) {
        this.idx = planner.getPlannerIndex();
        this.date = planner.getPlannerDate();
        this.memo = planner.getPlannerMemo();
        this.dailyStudyTime = planner.getPlannerDailyStudyTime();
        this.dailyScheduleNum = planner.getPlannerDailyScheduleNum();
        this.isPublic = planner.getPlannerIsPublic();
        this.dday = planner.getPlannerDday();
    }
}
