package com.mmos.mmos.src.domain.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class ProjectSaveRequestDto {
    private LocalDate startTime;
    private LocalDate endTime;
    private String name;
    private Boolean isStudy;
    private Long studyIdx;
    private String memo;
    private String place;
}
