package com.mmos.mmos.src.domain.dto.request;

import lombok.Getter;

@Getter
public class CalendarGetRequestDto {
    Integer month;
    Integer year;

    public CalendarGetRequestDto(Integer month, Integer year) {
        this.month = month;
        this.year = year;
    }
}
