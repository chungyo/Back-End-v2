package com.mmos.mmos.src.domain.dto.response.home;

import com.mmos.mmos.src.domain.entity.Plan;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TodoSectionDto {

    // 오늘 계획들 조회 (최대 8개)
    // Map<계획 이름, 계획 완수 여부>
//    private Map<String, Boolean> planMap = new HashMap<>();
//
//    public TodoSectionDto(Map<String, Boolean> planMap) {
//        this.planMap = planMap;
//    }

    // 계획 인덱스
    Long planIdx;
    // 계획명
    String planName;
    // 계획 완수 여부
    Boolean isComplete;

    public TodoSectionDto(Plan plan) {
        this.planIdx = plan.getPlanIndex();
        this.planName = plan.getPlanName();
        this.isComplete = plan.getPlanIsComplete();
    }
}
