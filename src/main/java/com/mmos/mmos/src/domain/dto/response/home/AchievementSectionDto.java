package com.mmos.mmos.src.domain.dto.response.home;

import com.mmos.mmos.src.domain.entity.Badge;
import com.mmos.mmos.src.domain.entity.Streak;
import com.mmos.mmos.src.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class AchievementSectionDto {

    // 티어 사진
    private String tierIcon;
    // 티어 이름
    private String tierName;
    // 티어 분포도 (ex. 사용자 상위 20% -> 문구 전체)
    private String tierDistribution;
    // 티어 진행도 (ex. 50%)
    private Integer tierProgress;
    // 총 공부 시간 (시간 단위)
    private Long totalStudyTimes;
    // 계획 총 완수량
    private Long totalCompleteSchedules;
    // 총 계획량
    private Long totalSchedules;
    // 현재 스트릭 연속 일 수
    private Long currentStreakDays;
    // 최대 스트릭 연속 일 수
    private Long topStreakDays;
    // 최근 60일 스트릭 엔티티 조회
    private List<Streak> streakList = new ArrayList<>();

    public AchievementSectionDto(User user, Badge tier) {
        this.tierIcon = tier.getBadgeIcon();
        this.tierName = tier.getBadgeName();
        this.tierDistribution = tier.getBadgeInfo();
        this.totalStudyTimes = user.getUserTotalStudyTime() / 60L;
        this.totalCompleteSchedules = user.getUserTotalCompletedScheduleNum();
        this.totalSchedules = user.getUserTotalScheduleNum();
        this.currentStreakDays = user.getUserCurrentStreak();
        this.topStreakDays = user.getUserTopStreak();
        this.streakList = user.getStreaks();

        if(user.getUserTotalScheduleNum().equals(0L))
            this.tierProgress = (int) (user.getUserTotalStudyTime() * 4);
        else
            this.tierProgress = (int) ((user.getUserTotalStudyTime() * 4)
                                    + (user.getUserTotalCompletedScheduleNum()
                                    * (100 + ((user.getUserTotalCompletedScheduleNum() * 100)
                                    / user.getUserTotalScheduleNum()))) / 100
            );
    }
}
