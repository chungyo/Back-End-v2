package com.mmos.mmos.src.domain.dto.response.challenge;

import com.mmos.mmos.src.domain.entity.Badge;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChallengePageResponseDto {

    UserInfoSectionDto userInfoSectionDto;
    BadgeSectionDto badgeSectionDto;

    public ChallengePageResponseDto(Badge tier, List<Badge> myRepresentBadges, List<Badge> myBadges, List<Badge> allBadges) {
        this.userInfoSectionDto = new UserInfoSectionDto(tier, myRepresentBadges);
        this.badgeSectionDto = new BadgeSectionDto(myBadges, allBadges);
    }
}
