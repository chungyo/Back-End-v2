package com.mmos.mmos.src.domain.dto.university;

import com.mmos.mmos.src.domain.entity.College;
import com.mmos.mmos.src.domain.entity.University;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UniversityResponseDto {

    private Long idx;
    private String name;
    private List<College> collegeIdx;

    public UniversityResponseDto(University university) {
        this.idx = university.getUniversityIndex();
        this.name = university.getUniversityName();
        this.collegeIdx = university.getUniversityColleges();
    }
}
