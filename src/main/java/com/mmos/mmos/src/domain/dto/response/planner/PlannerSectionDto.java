package com.mmos.mmos.src.domain.dto.response.planner;

import com.mmos.mmos.src.domain.entity.Plan;
import com.mmos.mmos.src.domain.entity.Planner;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PlannerSectionDto {

    // 플래너 인덱스
    private Long plannerIdx;
    // 월
    private Integer month;
    // 일
    private Integer day;
    // 오늘 공부 시간
    private Long todayStudyTime;
    // Plan 리스트
    private List<Plan> plans = new ArrayList<>();

    public PlannerSectionDto(Planner planner) {
        this.plannerIdx = planner.getPlannerIndex();
        this.month = planner.getPlannerDate().getMonthValue();
        this.day = planner.getPlannerDate().getDayOfMonth();
        this.todayStudyTime = planner.getPlannerDailyStudyTime();
        this.plans = planner.getPlannerPlans();
    }
}
