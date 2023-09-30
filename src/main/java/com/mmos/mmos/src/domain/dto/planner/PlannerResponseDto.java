package com.mmos.mmos.src.domain.dto.planner;

import com.mmos.mmos.src.domain.entity.Plan;
import com.mmos.mmos.src.domain.entity.Planner;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class PlannerResponseDto {

    private Long idx;
    private LocalDate date;
    private String memo;
    private Long dailyStudyTime;
    private Long dailyScheduleNum;
    private List<Plan> plans;

    public PlannerResponseDto(Planner planner) {
        this.idx = planner.getPlannerIndex();
        this.date = planner.getPlannerDate();
        this.memo = planner.getPlannerMemo();
        this.dailyStudyTime = planner.getPlannerDailyStudyTime();
        this.dailyScheduleNum = planner.getPlannerDailyScheduleNum();
        this.plans = planner.getPlannerPlans();

    }
}
