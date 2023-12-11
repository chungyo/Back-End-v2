package com.mmos.mmos.src.domain.dto.response.social;

import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.domain.entity.UserBadge;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RankingSectionDto {
    // 티어
    String tier;
    // 프사
    String pfp;
    // 이름
    String name;
    // 아이디
    String id;
    // 이번 주 공부 시간
    Long WeeklyStudyTime;

    public RankingSectionDto(User user) {
        for (UserBadge userUserbadge : user.getUserUserbadges()) {
            if (userUserbadge.getBadge().getBadgePurpose().equals("pfp") && userUserbadge.getUserbadgeIsVisible())
                this.pfp = userUserbadge.getBadge().getBadgeIcon();
            if (userUserbadge.getBadge().getBadgePurpose().equals("tier") && userUserbadge.getUserbadgeIsVisible())
                this.tier = userUserbadge.getBadge().getBadgeIcon();
        }
        this.name = user.getName();
        this.id = user.getUserId();
        this.WeeklyStudyTime = user.getUserWeeklyStudyTime();
    }
}
