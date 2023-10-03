package com.mmos.mmos.src.domain.dto.college;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.src.domain.entity.College;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CollegeResponseDto {

    private Long idx;
    private String name;

    private HttpResponseStatus status;

    public CollegeResponseDto(College college, HttpResponseStatus status) {
        this.idx = college.getCollegeIndex();
        this.name = college.getCollegeName();
        this.status = status;
    }

    public CollegeResponseDto(HttpResponseStatus status) {
        this.status = status;
    }
}
