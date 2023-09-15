package com.mmos.mmos.src.domain.dto.planner;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PlannerSaveRequestDto {

    LocalDate plannerDate;
    Long calendarIdx;

}
