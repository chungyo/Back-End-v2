package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.EmptyEntityException;
import com.mmos.mmos.src.domain.dto.request.BadgeUpdateRequestDto;
import com.mmos.mmos.src.domain.entity.Badge;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.domain.entity.UserBadge;
import com.mmos.mmos.src.repository.UserBadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.DATABASE_ERROR;
import static com.mmos.mmos.config.HttpResponseStatus.EMPTY_USERBADGE;

@Service
@RequiredArgsConstructor
public class UserBadgeService {
    private final UserBadgeRepository userBadgeRepository;
    private final UserService userService;
    private final BadgeService badgeService;

    public UserBadge findUserBadgeByIdx(Long userBadgeIdx) throws BaseException {
        return userBadgeRepository.findById(userBadgeIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USERBADGE));
    }

    public List<UserBadge> findUserBadgesByUserIndexAndBadgePurpose(Long userIdx, String purpose) throws BaseException {
        return userBadgeRepository.findUserBadgesByUser_UserIndexAndBadge_BadgePurpose(userIdx, purpose)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USERBADGE));
    }

    public List<UserBadge> findRepresentUserBadgesByUserIndexAndBadgePurpose(Long userIdx, String purpose) throws BaseException {
        return userBadgeRepository.findUserBadgeByUser_UserIndexAndBadge_BadgePurposeAndUserbadgeIsVisibleIsTrue(userIdx, purpose)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USERBADGE));
    }

    @Transactional
    public void saveUserBadge(Long userIdx) throws BaseException {
        try {
            System.out.println(true);
            User user = userService.getUser(userIdx);
            List<Badge> badges = badgeService.findAllBadges();

            List<Badge> newBadges = new ArrayList<>();
            for (Badge badge : badges) {
                if((badge.getIsBadgeTime() && user.getUserTotalStudyTime() >= badge.getBadgeExp())
                        || (!badge.getIsBadgeTime() && user.getUserTotalCompletedScheduleNum() >= badge.getBadgeExp())
                        || (user.getUserTotalScheduleNum() != 0 && (badge.getBadgePurpose().equals("tier") &&
                            (((user.getUserTotalStudyTime() * 4) + (user.getUserTotalCompletedScheduleNum() * (100 + ((user.getUserTotalCompletedScheduleNum() * 100)
                            / user.getUserTotalScheduleNum()))) / 100) >= badge.getBadgeExp()))
                        || (user.getUserTotalScheduleNum() == 0 && (badge.getBadgePurpose().equals("tier") &&
                            (user.getUserTotalStudyTime() * 4 >= badge.getBadgeExp()))))
                )
                {
                    // 유저가 이미 해당 뱃지를 존재하는 경우 continue
                    boolean isExist = false;
                    for(UserBadge userOwnBadge : user.getUserUserbadges())
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
                        newBadges.add(userBadge.getBadge());

                        if(badge.getBadgePurpose().equals("tier")) {
                            List<UserBadge> prevTier = getRepresentBadges(userIdx, "tier");
                            if(prevTier.isEmpty()) {
                                userBadge.updateIsVisible(true);
                            } else {
                                if(badge.getBadgeExp() > prevTier.get(0).getBadge().getBadgeExp()) {
                                    userBadge.updateIsVisible(true);
                                    prevTier.get(0).updateIsVisible(false);
                                }
                            }
                        } else if(badge.getBadgePurpose().equals("pfp")) {
                            List<UserBadge> myPfp = getRepresentBadges(userIdx, "pfp");
                            if(myPfp.isEmpty()) {
                                userBadge.updateIsVisible(true);
                            }
                        }
                    }
                }
            }

        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 내 도전과제 전체 조회
    @Transactional
    public List<Badge> getBadges(Long userIdx) throws BaseException {
        try {
            List<UserBadge> userBadgeList = findUserBadgesByUserIndexAndBadgePurpose(userIdx, "badge");
            List<Badge> responseDtoList = new ArrayList<>();
            if (userBadgeList.isEmpty()) {
                responseDtoList = null;
            } else {
                for (UserBadge userBadge : userBadgeList) {
                    responseDtoList.add(userBadge.getBadge());
                }
            }

            return responseDtoList;
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<UserBadge> getRepresentBadges(Long userIdx, String purpose) throws BaseException {
        try {
            return findRepresentUserBadgesByUserIndexAndBadgePurpose(userIdx, purpose);
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<Badge> setRepresentBadges(Long userIdx, BadgeUpdateRequestDto requestDto) throws BaseException {
        try {
            List<Badge> badges = new ArrayList<>();
            List<UserBadge> userBadges = getRepresentBadges(userIdx, "badge");
            for (UserBadge userBadge : userBadges) {
                userBadge.updateIsVisible(false);
            }

            for (Long userBadgeidx : requestDto.getUserBadgeIdxList()) {
                UserBadge userBadge = findUserBadgeByIdx(userBadgeidx);
                userBadge.updateIsVisible(true);
                badges.add(userBadge.getBadge());
            }

            return badges;
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void updatePfp(UserBadge userBadge, boolean status) throws BaseException {
        try {
            userBadge.updateIsVisible(status);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
