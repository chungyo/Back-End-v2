package com.mmos.mmos.src.domain.dto.major;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.src.domain.entity.Major;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MajorResponseDto {

    private Long idx;
    private String name;
    private HttpResponseStatus status;

    public MajorResponseDto(Major major, HttpResponseStatus status) {
        this.idx = major.getMajorIndex();
        this.name = major.getMajorName();
        this.status = status;
    }

    public MajorResponseDto(Major major) {
        this.idx = major.getMajorIndex();
        this.name = major.getMajorName();
    }

    public MajorResponseDto(HttpResponseStatus status) {
        this.status = status;
    }
}
