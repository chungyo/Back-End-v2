package com.mmos.mmos.src.domain.dto.study;

import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.domain.entity.UserStudy;
import lombok.Getter;

import java.util.List;

@Getter
public class StudyResponseDto {

    private Long index;
    private List<UserStudy> userstudies;
    private Integer memberLimit;
    private String name;
    private Boolean isVisible;
    private Boolean isComplete;
    public StudyResponseDto(Study study) {
        this.index = study.getStudyIndex();
        this.userstudies = study.getStudyUserstudies();
        this.memberLimit = study.getStudyMemberLimit();
        this.name = study.getStudyName();
        this.isVisible = study.getStudyIsVisible();
        this.isComplete = study.getStudyIsComplete();
    }
}
