package com.mmos.mmos.src.domain.dto.badge;

import com.mmos.mmos.src.domain.entity.Badge;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BadgeResponseDto {
    private Long badgeIdx;
    private Long badgeExp;
    private String badgeIcon;
    private String badgeInfo;
    private String badgeName;
    private Boolean badgeStatus;


    public BadgeResponseDto(Badge entity) {
        this.badgeIdx = entity.getBadgeIndex();
        this.badgeExp = entity.getBadgeExp();
        this.badgeIcon = entity.getBadgeIcon();
        this.badgeInfo = entity.getBadgeInfo();
        this.badgeName = entity.getBadgeName();
    }
}
