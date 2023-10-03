package com.mmos.mmos.src.domain.dto.streak;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.src.domain.entity.Streak;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StreakResponseDto {
    private Long idx;
    private Integer level;
    private LocalDate date;
    private HttpResponseStatus status;


    public StreakResponseDto(Streak streak, HttpResponseStatus status) {
        this.idx = streak.getStreakIndex();
        this.level = streak.getStreakLevel();
        this.date = streak.getStreakDate();
        this.status = status;
    }

    public StreakResponseDto(Streak streak) {
        this.idx = streak.getStreakIndex();
        this.level = streak.getStreakLevel();
        this.date = streak.getStreakDate();
    }

    public StreakResponseDto(HttpResponseStatus status) {
        this.status = status;
    }
}
