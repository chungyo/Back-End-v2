package com.mmos.mmos.src.domain.dto.study;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.src.domain.entity.Study;
import lombok.Getter;

@Getter
public class StudyResponseDto {

    private Long index;
    private Integer memberLimit;
    private String name;
    private Boolean isVisible;
    private Boolean isComplete;
    private HttpResponseStatus status;

    public StudyResponseDto(Study study) {
        this.index = study.getStudyIndex();
        this.memberLimit = study.getStudyMemberLimit();
        this.name = study.getStudyName();
        this.isVisible = study.getStudyIsVisible();
        this.isComplete = study.getStudyIsComplete();
    }

    public StudyResponseDto(HttpResponseStatus status) {
        this.status = status;
    }
}
