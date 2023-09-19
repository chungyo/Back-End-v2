package com.mmos.mmos.src.domain.dto.badge;

import com.mmos.mmos.src.domain.entity.Badge;
import lombok.Builder;
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

    @Builder
    public BadgeResponseDto(Badge entity) {
        this.badgeIdx = entity.getBadge_index();
        this.badgeExp = entity.getBadge_exp();
        this.badgeIcon = entity.getBadge_icon();
        this.badgeInfo = entity.getBadge_info();
        this.badgeName = entity.getBadge_name();
        this.badgeStatus = entity.getBadge_status();
    }
}
