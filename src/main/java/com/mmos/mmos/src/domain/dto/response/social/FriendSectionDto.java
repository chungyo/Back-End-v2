package com.mmos.mmos.src.domain.dto.response.social;

import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Planner;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.domain.entity.UserBadge;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class FriendSectionDto {
    // 티어
    String tier;
    // 프사
    String pfp;
    // 이름
    String name;
    // 닉네임
    String nickname;
    // 아이디
    String id;
    // 이번 주 공부 시간
    Long WeeklyStudyTime;
    // 오늘 공부 시간
    Long DailyStudyTime;

    public FriendSectionDto(User user) {
        for (UserBadge userUserbadge : user.getUserUserbadges()) {
            if (userUserbadge.getBadge().getBadgePurpose().equals("pfp") && userUserbadge.getUserbadgeIsVisible())
                this.pfp = userUserbadge.getBadge().getBadgeIcon();
            if (userUserbadge.getBadge().getBadgePurpose().equals("tier") && userUserbadge.getUserbadgeIsVisible())
                this.tier = userUserbadge.getBadge().getBadgeIcon();
        }
        this.name = user.getUserName();
        this.nickname = user.getUserNickname();
        this.id = user.getUserId();
        this.WeeklyStudyTime = user.getUserWeeklyStudyTime();
        for (Calendar userCalendar : user.getUserCalendars()) {
            if(userCalendar.getCalendarYear().equals(LocalDate.now().getYear()) &&
                    userCalendar.getCalendarMonth().equals(LocalDate.now().getMonthValue()))
                for (Planner calendarPlanner : userCalendar.getCalendarPlanners()) {
                    if(calendarPlanner.getPlannerDate().equals(LocalDate.now()))
                        this.DailyStudyTime = calendarPlanner.getPlannerDailyStudyTime();
                }
        }
    }
}
