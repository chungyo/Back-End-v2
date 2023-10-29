package com.mmos.mmos.src.domain.dto.response.challenge;

import com.mmos.mmos.src.domain.entity.Badge;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BadgeSectionDto {

    List<Badge> allMyBadges;
    List<Badge> allBadges;

    public BadgeSectionDto(List<Badge> allMyBadges, List<Badge> allBadges) {
        this.allMyBadges = allMyBadges;
        this.allBadges = allBadges;
    }
}
