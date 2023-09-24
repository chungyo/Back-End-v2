package com.mmos.mmos.src.domain.dto.userbadge;

import com.mmos.mmos.src.domain.entity.UserBadge;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserBadgeResponseDto {

    private Long idx;
    private Boolean isVisible;

    public UserBadgeResponseDto(UserBadge entity) {
        this.idx = entity.getUserbadgeIndex();
        this.isVisible = entity.getUserbadgeIsVisible();
    }
}
