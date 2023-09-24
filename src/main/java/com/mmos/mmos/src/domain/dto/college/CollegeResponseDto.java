package com.mmos.mmos.src.domain.dto.college;

import com.mmos.mmos.src.domain.entity.College;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CollegeResponseDto {

    private Long idx;
    private String name;

    public CollegeResponseDto(College college) {
        this.idx = college.getCollegeIndex();
        this.name = college.getCollegeName();
    }
}
