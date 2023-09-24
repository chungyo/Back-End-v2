package com.mmos.mmos.src.domain.dto.plan;

import com.mmos.mmos.src.domain.entity.Plan;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class PlanSaveRequestDto {

    String planName;
    Boolean isStudy;
    Long userStudyIdx;

    public PlanSaveRequestDto(Plan plan) {
        this.planName = plan.getPlanName();
        this.isStudy = plan.getPlanIsStudy();
        this.userStudyIdx = plan.getUserStudy().getUserstudyIndex();
    }
}
