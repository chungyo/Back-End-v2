package com.mmos.mmos.src.domain.dto.studytime;

import com.mmos.mmos.src.domain.entity.StudyTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Getter
@RequiredArgsConstructor
public class StudyTimeResponseDto {

    private Long idx;
    private Timestamp startTime;
    private Timestamp endTime;

    public StudyTimeResponseDto(StudyTime studyTime) {
        this.idx = studyTime.getStudytimeIndex();
        this.startTime = studyTime.getStudytimeStartTime();
        this.endTime = studyTime.getStudytimeEndTime();
    }
}
