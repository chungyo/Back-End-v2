package com.mmos.mmos.src.domain.dto.response.home;

import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.domain.entity.UserBadge;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendSectionDto {

    // 친구 아이디
    String id;
    // 친구 이름
    String name;
    // 친구 프사
    String pfp;
    // 친구 오늘 공부 시간 (시:분)
    Long todayStudyTime;

    public FriendSectionDto(User friend) {
        this.id = friend.getUserId();
        this.name = friend.getUsername();
        this.todayStudyTime = friend.getUserTotalStudyTime();

        for (UserBadge userUserbadge : friend.getUserUserbadges()) {
            if(userUserbadge.getBadge().getBadgePurpose().equals("pfp")
                && userUserbadge.getUserbadgeIsVisible())
                this.pfp = userUserbadge.getBadge().getBadgeIcon();
        }
    }
}
