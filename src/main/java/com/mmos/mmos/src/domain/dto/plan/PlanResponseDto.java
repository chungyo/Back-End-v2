package com.mmos.mmos.src.domain.dto.plan;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.src.domain.entity.Plan;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class PlanResponseDto {

    private Long idx;
    private String name;
    private Boolean isComplete;
    private Boolean isStudy;
    private Boolean isVisible;
    private Long studyTime;
    private HttpResponseStatus status = null;
    private LocalDate date;
    public PlanResponseDto(Plan plan, HttpResponseStatus status) {
        this.idx = plan.getPlanIndex();
        this.name = plan.getPlanName();
        this.isComplete = plan.getPlanIsComplete();
        this.isStudy = plan.getPlanIsStudy();
        this.isVisible = plan.getPlanIsVisible();
        this.studyTime = plan.getPlanStudyTime();
        this.status = status;
    }

    public PlanResponseDto(Plan plan, LocalDate date) {
        this.idx = plan.getPlanIndex();
        this.name = plan.getPlanName();
        this.isComplete = plan.getPlanIsComplete();
        this.isStudy = plan.getPlanIsStudy();
        this.studyTime = plan.getPlanStudyTime();
        this.date = date;
    }
    public PlanResponseDto(HttpResponseStatus status) {
        this.status = status;
    }
}
