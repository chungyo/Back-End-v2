package com.mmos.mmos.src.domain.dto.request;

import com.mmos.mmos.src.domain.entity.Study;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudySaveRequestDto {
    private Integer memberLimit;
    private String name;
    private String memo;

    public StudySaveRequestDto(Study entity) {
        this.memberLimit = entity.getStudyMemberLimit();
        this.name = entity.getStudyName();
        this.memo = entity.getStudyMemo();
    }
}
