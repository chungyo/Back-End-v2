package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.streak.StreakResponseDto;
import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Planner;
import com.mmos.mmos.src.domain.entity.Streak;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.StreakRepository;
import com.mmos.mmos.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.ALREADY_EXIST_STREAK;

@Service
@RequiredArgsConstructor
public class StreakService {
    private final StreakRepository streakRepository;
    private final UserRepository userRepository;

    public User findUserByIdx(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElse(null);
    }

    public List<Streak> findStreaks60Days(User user) {
        return streakRepository.findTop60ByUserOrderByStreakIndexDesc(user)
                .orElse(null);
    }

    public void updateTopStreak(User user) {
        if(user.getUserTopStreak() < user.getUserCurrentStreak())
            user.updateTopStreak(user.getUserCurrentStreak());
    }

    @Transactional
    public StreakResponseDto saveStreak(Long userIdx) {
        User user = findUserByIdx(userIdx);
        LocalDate beforeDay = LocalDate.now().minusDays(1);

        for (Streak streak : user.getStreaks()) {
            if(streak.getStreakDate().equals(beforeDay))
                return new StreakResponseDto(ALREADY_EXIST_STREAK);
        }

        Planner planner = null;
        for (Calendar userCalendar : user.getUserCalendars()) {
            boolean flag = false;
            if(userCalendar.getCalendarYear().equals(beforeDay.getYear()) && userCalendar.getCalendarMonth().equals(beforeDay.getMonthValue())) {
                for (Planner calendarPlanner : userCalendar.getCalendarPlanners()) {
                    if(calendarPlanner.getPlannerDate().equals(beforeDay)) {
                        planner = calendarPlanner;
                        flag = true;
                        break;
                    }
                }
            }

            if(flag)
                break;
        }


        if(planner == null) {
            user.resetCurrentStreak();
            Streak streak = streakRepository.save(new Streak(0, beforeDay, user));
            user.addStreak(streak);
            return new StreakResponseDto(streak);
        }
        else {
            if(planner.getPlannerDailyStudyTime() >= 5 * 60) {
                user.plusCurrentStreak();
                updateTopStreak(user);
                Streak streak = streakRepository.save(new Streak(3, beforeDay, user));
                user.addStreak(streak);
                return new StreakResponseDto(streak);
            }
            else if(planner.getPlannerDailyStudyTime() >= 3 * 60) {
                user.plusCurrentStreak();
                updateTopStreak(user);
                Streak streak = streakRepository.save(new Streak(2, beforeDay, user));
                user.addStreak(streak);
                return new StreakResponseDto(streak);
            }
            else if(planner.getPlannerDailyStudyTime() >= 60) {
                user.plusCurrentStreak();
                updateTopStreak(user);
                Streak streak = streakRepository.save(new Streak(1, beforeDay, user));
                user.addStreak(streak);
                return new StreakResponseDto(streak);
            }
            else {
                user.resetCurrentStreak();
                Streak streak = streakRepository.save(new Streak(0, beforeDay, user));
                user.addStreak(streak);
                return new StreakResponseDto(streak);
            }
        }
    }

    @Transactional
    public List<StreakResponseDto> getStreaks(Long userIdx) {
        User user = findUserByIdx(userIdx);
        List<StreakResponseDto> responseDtoList = new ArrayList<>();
        List<Streak> streakList = findStreaks60Days(user);

        for (Streak streak : streakList) {
            responseDtoList.add(new StreakResponseDto(streak));
        }

        return responseDtoList;
    }
}
