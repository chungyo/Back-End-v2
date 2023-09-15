package com.mmos.mmos.src.domain.dto.badge;

import com.mmos.mmos.src.domain.entity.Badge;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BadgeResponseDto {
    private Long badgeIdx;
    private String badgeExp;
    private String badgeIcon;
    private String badgeInfo;
    private String badgeName;
    private boolean badgeStatus;


    public BadgeResponseDto(Badge entity) {
        this.badgeIdx = entity.getBadge_index();
        this.badgeExp = entity.getBadge_exp();
        this.badgeIcon = entity.getBadge_icon();
        this.badgeInfo = entity.getBadge_info();
        this.badgeName = entity.getBadge_name();
        this.badgeStatus = entity.isBadge_status();
    }
}
