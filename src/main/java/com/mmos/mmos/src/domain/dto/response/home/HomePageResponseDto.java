package com.mmos.mmos.src.domain.dto.response.home;

import com.mmos.mmos.src.domain.entity.Badge;
import com.mmos.mmos.src.domain.entity.Plan;
import com.mmos.mmos.src.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class HomePageResponseDto {

    private HomeSectionDto home;
    private AchievementSectionDto achievement;
    private List<TodoSectionDto> todo = new ArrayList<>();
    private CalendarSectionDto calendar;
    private List<FriendSectionDto> friend = new ArrayList<>();

    public HomePageResponseDto(User user,
                               List<Plan> plans,
                               CalendarSectionDto calendar,
                               List<User> friends,
                               Badge tier,
                               List<Badge> badges,
                               Badge pfp) {
        // HomeSection
       this.home = new HomeSectionDto(user, badges, pfp);

        // Achievement Section
        this.achievement = new AchievementSectionDto(user, tier);

        // Today's To Do Section
        for (Plan plan : plans) {
            todo.add(new TodoSectionDto(plan));
        }

        // Calendar Section
        this.calendar = calendar;

        // Friend Section
        for (User myFriend : friends) {
            friend.add(new FriendSectionDto(myFriend));
        }
    }
}
