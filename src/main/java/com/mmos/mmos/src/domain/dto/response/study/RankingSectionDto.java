package com.mmos.mmos.src.domain.dto.response.study;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RankingSectionDto {
    // 스터디 이름
    String name;
    // 스터디 평균 공부 시간
    Long studyTime;


}
