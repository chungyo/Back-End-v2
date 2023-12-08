package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.NotAuthorizedAccessException;
import com.mmos.mmos.src.domain.dto.request.CalendarGetRequestDto;
import com.mmos.mmos.src.domain.dto.response.social.FriendPlannerResponseDto;
import com.mmos.mmos.src.domain.dto.response.social.SocialPageResponseDto;
import com.mmos.mmos.src.domain.entity.*;
import com.mmos.mmos.src.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@RestController
@RequestMapping("/api/v1/social")
@RequiredArgsConstructor
public class SocialPageController extends BaseController {

    private final FriendService friendService;
    private final UserService userService;
    private final UserBadgeService userBadgeService;
    private final PlannerService plannerService;
    private final CalendarService calendarService;

    @GetMapping("")
    public ResponseEntity<ResponseApiMessage> getPage(@AuthenticationPrincipal User tokenUser) {
        try {
            Long userIdx = tokenUser.getUserIndex();
            // 기본 쿼리
            userBadgeService.saveUserBadge(userIdx);
            friendService.friendWithMe(userIdx);

            // 로직
            List<User> friends = friendService.getFriends(userIdx, 1);
            List<User> top3 = friendService.getTop3Friends(userIdx, 1);

            return sendResponseHttpByJson(SUCCESS, "소셜 페이지 로드 성공", new SocialPageResponseDto(friends, top3));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 친구 요청 보내기
    @PostMapping("/request/{friendId}")
    public ResponseEntity<ResponseApiMessage> sendFriendRequest(@AuthenticationPrincipal User tokenUser, @PathVariable String friendId) {
        try {
            Friend request = friendService.sendFriendRequest(tokenUser.getUserIndex(), friendId);

            return sendResponseHttpByJson(SUCCESS, "친구 추가 성공", request);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 친구 요청 수락
    @PatchMapping("/request/{friendIdx}")
    public ResponseEntity<ResponseApiMessage> acceptFriendRequest(@AuthenticationPrincipal User tokenUser, @PathVariable Long friendIdx) {
        try {
            Friend response = friendService.acceptFriendRequest(tokenUser.getUserIndex(), friendIdx);
            List<Friend> myFriends = response.getUser().getUserFriends();

            return sendResponseHttpByJson(SUCCESS, "친구 요청 수락 성공", myFriends);
        } catch (BaseException e) {
            e.printStackTrace();
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 친구 요청 거부/취소/친구 삭제
    @DeleteMapping("/request/{friendIdx}")
    public ResponseEntity<ResponseApiMessage> rejectFriendRequest(@AuthenticationPrincipal User tokenUser, @PathVariable Long friendIdx) {
        try {
            Integer friendStatus = friendService.deleteFriendRequest(tokenUser.getUserIndex(), friendIdx);
            List<User> myFriends = friendService.getFriends(tokenUser.getUserIndex(), friendStatus);

            return sendResponseHttpByJson(SUCCESS, "친구 요청 삭제", myFriends);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 받은 친구 요청 목록
    @GetMapping("/receive")
    public ResponseEntity<ResponseApiMessage> getReceivedFriendRequests(@AuthenticationPrincipal User tokenUser) {
        try {
            List<User> requestList = friendService.getFriends(tokenUser.getUserIndex(), 3);

            return sendResponseHttpByJson(SUCCESS, "받은 친구 요청 목록 조회 성공", requestList);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 보낸 친구 요청 목록
    @GetMapping("/send")
    public ResponseEntity<ResponseApiMessage> getSentFriendRequests(@AuthenticationPrincipal User tokenUser) {
        try {
            List<User> requestList = friendService.getFriends(tokenUser.getUserIndex(), 2);

            return sendResponseHttpByJson(SUCCESS, "보낸 친구 요청 목록 조회 성공", requestList);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 친구 상단 고정
    @PatchMapping("/fix/{friendIdx}")
    public ResponseEntity<ResponseApiMessage> updateIsFixed(@AuthenticationPrincipal User tokenUser, @PathVariable Long friendIdx) {
        try {
            friendService.updateIsFixed(tokenUser.getUserIndex(), friendIdx);
            List<User> myFriends = friendService.getFriends(tokenUser.getUserIndex(), 1);

            return sendResponseHttpByJson(SUCCESS, "친구 상단 고정/해제 성공", myFriends);
        } catch (BaseException e) {
            e.printStackTrace();
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 친구 플래너 확인
    @GetMapping("/friendInfo/{friendUserIdx}")
    public ResponseEntity<ResponseApiMessage> getFriendPlanner(@AuthenticationPrincipal User tokenUser, @PathVariable Long friendUserIdx) {
        try {
            User friend = userService.getUser(friendUserIdx);
            if(!friend.getIsPlannerVisible())
                throw new NotAuthorizedAccessException(FORBIDDEN_PLANNER);
            if(!friendService.getFriends(tokenUser.getUserIndex(), 1).contains(friend))
                throw new BaseException(EMPTY_FRIEND);

            Badge tier = userBadgeService.getRepresentBadges(friendUserIdx, "tier").get(0).getBadge();

            List<Badge> badges = new ArrayList<>();
            List<UserBadge> userBadges = userBadgeService.getRepresentBadges(tokenUser.getUserIndex(), "badge");
            for (UserBadge userBadge : userBadges) {
                badges.add(userBadge.getBadge());
            }

            Badge pfp = userBadgeService.getRepresentBadges(friendUserIdx, "pfp").get(0).getBadge();

            Planner planner = plannerService.getPlannerByCalendarAndDate(calendarService.getCalendar(
                    friendUserIdx,
                    new CalendarGetRequestDto(LocalDate.now().getMonthValue(),
                                                LocalDate.now().getYear())).getIdx(),
                                                LocalDate.now());

            return sendResponseHttpByJson(SUCCESS, "친구 플래너 조회 성공", new FriendPlannerResponseDto(friend, tier, badges, pfp, planner));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

}
