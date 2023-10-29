package com.mmos.mmos.src.domain.dto.response.challenge;

import com.mmos.mmos.src.domain.entity.Badge;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserInfoSectionDto {

    // 티어
    Badge tier;
    // 대표 도전과제
    List<Badge> badges = new ArrayList<>();

    public UserInfoSectionDto(Badge tier, List<Badge> badges) {
        this.tier = tier;
        this.badges = badges;
    }
}
