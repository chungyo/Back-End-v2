package com.mmos.mmos.src.domain.dto.university;

import com.mmos.mmos.src.domain.entity.University;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UniversityResponseDto {

    private Long idx;
    private String name;

    public UniversityResponseDto(University university) {
        this.idx = university.getUniversityIndex();
        this.name = university.getUniversityName();
    }
}
