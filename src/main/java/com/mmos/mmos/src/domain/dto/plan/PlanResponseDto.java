package com.mmos.mmos.src.domain.dto.plan;

import com.mmos.mmos.src.domain.entity.Plan;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlanResponseDto {

    private Long planIdx;
    private String planName;
    private Boolean isComplete;
    private Boolean isStudy;

    public PlanResponseDto(Plan plan) {
        this.planIdx = plan.getPlanIndex();
        this.planName = plan.getPlanName();
        this.isComplete = plan.getPlanIsComplete();
        this.isStudy = plan.getPlanIsStudy();
    }
}
