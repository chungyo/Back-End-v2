package com.mmos.mmos.src.domain.dto.university;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.src.domain.entity.University;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UniversityResponseDto {

    private Long idx;
    private String name;
    private HttpResponseStatus status;

    public UniversityResponseDto(University university, HttpResponseStatus status) {
        this.idx = university.getUniversityIndex();
        this.name = university.getUniversityName();
        this.status = status;
    }
}
