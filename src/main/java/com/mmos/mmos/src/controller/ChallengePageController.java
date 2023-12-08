package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.src.domain.dto.request.BadgeUpdateRequestDto;
import com.mmos.mmos.src.domain.dto.response.challenge.ChallengePageResponseDto;
import com.mmos.mmos.src.domain.entity.Badge;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.domain.entity.UserBadge;
import com.mmos.mmos.src.service.BadgeService;
import com.mmos.mmos.src.service.UserBadgeService;
import com.mmos.mmos.src.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;
@RestController
@RequestMapping("/api/v1/challenge")
@RequiredArgsConstructor
public class ChallengePageController extends BaseController {

    private final UserBadgeService userBadgeService;
    private final BadgeService badgeService;

    @GetMapping("")
    public ResponseEntity<ResponseApiMessage> getPage(@AuthenticationPrincipal User tokenUser) {
        try {
            Long userIdx = tokenUser.getUserIndex();
            // 기본 쿼리
            userBadgeService.saveUserBadge(userIdx);

            // 로직
            Badge tier = userBadgeService.getRepresentBadges(userIdx, "tier").get(0).getBadge();
            List<Badge> myAllBadges = userBadgeService.getBadges(userIdx);
            List<UserBadge> myRepresentUserBadge = userBadgeService.getRepresentBadges(userIdx, "badge");
            List<Badge> myRepresentBadge = new ArrayList<>();
            for (UserBadge userBadge : myRepresentUserBadge) {
                myRepresentBadge.add(userBadge.getBadge());
            }
            List<Badge> allBadges = badgeService.getBadgesByPurpose("badge");

            return sendResponseHttpByJson(SUCCESS, "도전과제 페이지 로드 성공", new ChallengePageResponseDto(tier, myRepresentBadge, myAllBadges, allBadges));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 대표 뱃지 설정
    @PatchMapping("")
    public ResponseEntity<ResponseApiMessage> setRepresentBadges(@AuthenticationPrincipal User tokenUser, @RequestBody BadgeUpdateRequestDto requestDto) {
        try {
            return sendResponseHttpByJson(SUCCESS, "대표 뱃지 설정 성공", userBadgeService.setRepresentBadges(tokenUser.getUserIndex(), requestDto));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }
}
