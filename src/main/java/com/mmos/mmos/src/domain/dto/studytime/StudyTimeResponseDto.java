package com.mmos.mmos.src.domain.dto.studytime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Getter
@RequiredArgsConstructor
public class StudyTimeResponseDto {

    private Long studyTimeIdx;
    private Timestamp startTime;
    private Timestamp endTime;

    public StudyTimeResponseDto(Long studyTimeIdx, Timestamp startTime, Timestamp endTime) {
        this.studyTimeIdx = studyTimeIdx;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
