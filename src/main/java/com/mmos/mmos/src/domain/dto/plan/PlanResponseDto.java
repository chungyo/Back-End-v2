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

    public PlanResponseDto(Long planIdx, String planName, Boolean isComplete, Boolean isStudy) {
        this.planIdx = planIdx;
        this.planName = planName;
        this.isComplete = isComplete;
        this.isStudy = isStudy;
    }
}
