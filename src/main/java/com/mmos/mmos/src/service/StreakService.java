package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.DuplicateRequestException;
import com.mmos.mmos.config.exception.EmptyEntityException;
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
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@Service
@RequiredArgsConstructor
public class StreakService {
    private final StreakRepository streakRepository;
    private final UserRepository userRepository;

    public User findUserByIdx(Long userIdx) throws BaseException {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USER));
    }

    public List<Streak> findStreaks60Days(User user) throws BaseException {
        return streakRepository.findTop60ByUserOrderByStreakIndexDesc(user)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_STREAK));
    }

    public void updateTopStreak(User user) {
        if (user.getUserTopStreak() < user.getUserCurrentStreak())
            user.updateTopStreak(user.getUserCurrentStreak());
    }

    @Transactional
    public void saveStreak(Long userIdx) throws BaseException {
        try {
            User user = findUserByIdx(userIdx);
            LocalDate beforeDay = LocalDate.now().minusDays(1);

            for (Streak streak : user.getStreaks()) {
                if (streak.getStreakDate().equals(beforeDay))
                    throw new DuplicateRequestException(ALREADY_EXIST_STREAK);
            }

            Planner planner = null;
            for (Calendar userCalendar : user.getUserCalendars()) {
                boolean flag = false;
                if (userCalendar.getCalendarYear().equals(beforeDay.getYear()) &&
                        userCalendar.getCalendarMonth().equals(beforeDay.getMonthValue())) {
                    for (Planner calendarPlanner : userCalendar.getCalendarPlanners()) {
                        if (calendarPlanner.getPlannerDate().equals(beforeDay)) {
                            planner = calendarPlanner;
                            flag = true;
                            break;
                        }
                    }
                }

                if (flag)
                    break;
            }


            if (planner == null) {
                user.resetCurrentStreak();
                Streak streak = streakRepository.save(new Streak(0, beforeDay, user));
                user.addStreak(streak);
            } else {
                if (planner.getPlannerDailyStudyTime() >= 5 * 60) {
                    user.plusCurrentStreak();
                    updateTopStreak(user);
                    Streak streak = streakRepository.save(new Streak(3, beforeDay, user));
                    user.addStreak(streak);
                } else if (planner.getPlannerDailyStudyTime() >= 3 * 60) {
                    user.plusCurrentStreak();
                    updateTopStreak(user);
                    Streak streak = streakRepository.save(new Streak(2, beforeDay, user));
                    user.addStreak(streak);
                } else if (planner.getPlannerDailyStudyTime() >= 60) {
                    user.plusCurrentStreak();
                    updateTopStreak(user);
                    Streak streak = streakRepository.save(new Streak(1, beforeDay, user));
                    user.addStreak(streak);
                } else {
                    user.resetCurrentStreak();
                    Streak streak = streakRepository.save(new Streak(0, beforeDay, user));
                    user.addStreak(streak);
                }
            }
        } catch (EmptyEntityException |
                DuplicateRequestException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<Streak> getStreaks(Long userIdx) throws BaseException {
        try {
            User user = findUserByIdx(userIdx);
            return findStreaks60Days(user);
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
