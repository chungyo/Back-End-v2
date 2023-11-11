package com.mmos.mmos.src.domain.dto.response.social;

import com.mmos.mmos.src.domain.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class FriendPlannerResponseDto {
    // 이름
    String name;
    // 아이디
    String id;
    // 학교
    String universityName;
    // 전공
    String majorName;
    // 티어
    String tier;
    // 프사
    String pfp;
    // 도전과제
    List<String> badges = new ArrayList<>();
    // 스트릭
    List<Streak> streaks = new ArrayList<>();
    // 오늘 계획
    Planner planner;

    public FriendPlannerResponseDto(User friend, Badge tier, List<Badge> badges, Badge pfp, Planner planner) {
        this.name = friend.getUsername();
        this.id = friend.getUserId();
        this.universityName = friend.getMajor().getCollege().getUniversity().getUniversityName();
        this.majorName = friend.getMajor().getMajorName();
        this.tier = tier.getBadgeIcon();
        this.pfp = pfp.getBadgeIcon();
        for (Badge badge : badges) {
            this.badges.add(badge.getBadgeIcon());
        }
        this.streaks = friend.getStreaks();
        this.planner = planner;
    }
}
