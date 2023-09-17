package com.mmos.mmos.src.domain.dto.study;

import com.mmos.mmos.src.domain.entity.Study;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudySaveRequestDto {
    private Integer studyMemberLimit;
    private String studyName;

    public Study toEntity() {
        return Study.builder()
                .study_name(studyName)
                .study_member_limit(studyMemberLimit)
                .build();
    }
}
