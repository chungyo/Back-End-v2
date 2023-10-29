package com.mmos.mmos.src.domain.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyUpdateRequestDto {
    private String newName;
    private String newMemo;
    private Boolean isComplete;
}
