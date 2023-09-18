package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.entity.Badge;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.domain.entity.UserBadge;
import com.mmos.mmos.src.repository.BadgeRepository;
import com.mmos.mmos.src.repository.UserBadgeRepository;
import com.mmos.mmos.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBadgeService {
    private final UserBadgeRepository userBadgeRepository;
    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;

    public User findUser(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. USER_INDEX=" + userIdx));
    }

    public List<Badge> findBadges () {
        return badgeRepository.findAll();
    }

    public List<UserBadge> saveUserBadge(Long userIdx){
        User user = findUser(userIdx);
        List<Badge> badges = findBadges();

        List<UserBadge> newBadges = new ArrayList<>();
        for (Badge badge : badges) {
            if((badge.is_badge_time() && user.getUser_total_study_time() >= badge.getBadge_exp())
                    || (!badge.is_badge_time() && user.getUser_total_completed_schedule_num() >= badge.getBadge_exp()))
            {
                // 유저가 이미 해당 뱃지를 존재하는 경우 continue
                boolean isExist = false;
                for(UserBadge userOwnBadge : user.getUser_userbadges())
                    if (userOwnBadge.getBadge() == badge) {
                        isExist = true;
                        break;
                    }
                if(!isExist) {
                    // 뱃지 생성
                    UserBadge userBadge = new UserBadge(user, badge);
                    // 추가
                    user.addUserBadges(userBadge);
                    userBadgeRepository.save(userBadge);
                    newBadges.add(userBadge);
                }
            }
        }
        return newBadges;
    }
}
