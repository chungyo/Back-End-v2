package com.mmos.mmos.src.domain.dto.project;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProjectTimeUpdateDto {
    private LocalDate newStartTime;
    private LocalDate newEndTime;
}
