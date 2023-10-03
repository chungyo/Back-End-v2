package com.mmos.mmos.src.domain.dto.badge;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.src.domain.entity.Badge;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BadgeResponseDto {
    private Long idx;
    private Long exp;
    private String icon;
    private String info;
    private String name;

    private HttpResponseStatus status;


    public BadgeResponseDto(Badge entity, HttpResponseStatus status) {
        this.idx = entity.getBadgeIndex();
        this.exp = entity.getBadgeExp();
        this.icon = entity.getBadgeIcon();
        this.info = entity.getBadgeInfo();
        this.name = entity.getBadgeName();
        this.status = status;
    }

    public BadgeResponseDto(HttpResponseStatus status) {
        this.status = status;
    }

}
