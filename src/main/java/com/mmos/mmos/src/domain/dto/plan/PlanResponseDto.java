package com.mmos.mmos.src.domain.dto.plan;

import com.mmos.mmos.src.domain.entity.Plan;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlanResponseDto {

    private Long idx;
    private String name;
    private Boolean isComplete;
    private Boolean isStudy;
    private Boolean isVisibleOnCalendar;

    public PlanResponseDto(Plan plan) {
        this.idx = plan.getPlanIndex();
        this.name = plan.getPlanName();
        this.isComplete = plan.getPlanIsComplete();
        this.isStudy = plan.getPlanIsStudy();
        this.isVisibleOnCalendar = plan.getPlanIsVisibleOnCalendar();
    }
}
