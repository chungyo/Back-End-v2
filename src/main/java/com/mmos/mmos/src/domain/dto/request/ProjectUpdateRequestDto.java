package com.mmos.mmos.src.domain.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class ProjectUpdateRequestDto {
    private String newName;
    private LocalDate newStartTime;
    private LocalDate newEndTime;
}
