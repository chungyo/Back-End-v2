package com.mmos.mmos.src.domain.dto.plan;

import com.mmos.mmos.src.domain.entity.Plan;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class PlanSaveRequestDto {

    private String planName;
    private Boolean isStudy;
    private Long userStudyIdx;
    private LocalDate date;

    public PlanSaveRequestDto(Plan plan) {
        this.planName = plan.getPlanName();
        this.isStudy = plan.getPlanIsStudy();
        this.userStudyIdx = plan.getUserStudy().getUserstudyIndex();
    }
}
