package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.src.domain.dto.request.CalendarGetRequestDto;
import com.mmos.mmos.src.domain.dto.response.social.FriendPlannerResponseDto;
import com.mmos.mmos.src.domain.dto.response.social.SocialPageResponseDto;
import com.mmos.mmos.src.domain.entity.*;
import com.mmos.mmos.src.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.EMPTY_FRIEND;
import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/social")
@RequiredArgsConstructor
public class SocialPageController extends BaseController {

    private final FriendService friendService;
    private final UserService userService;
    private final UserBadgeService userBadgeService;
    private final PlannerService plannerService;
    private final CalendarService calendarService;

    /** (미완수)
     * 친구 추가 기능을 완수하면 getTop3Friends 메서드 체크해봐야 함.
     * @param userIdx
     */
    @GetMapping("")
    public ResponseEntity<ResponseApiMessage> getPage(@RequestParam Long userIdx) {
        try {
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
    @PostMapping("/request")
    public ResponseEntity<ResponseApiMessage> sendFriendRequest(@RequestParam Long userIdx, @RequestParam String friendId) {
        try {
            Friend request = friendService.sendFriendRequest(userIdx, friendId);

            return sendResponseHttpByJson(SUCCESS, "친구 추가 성공", request);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 친구 요청 수락
    @PatchMapping("/request")
    public ResponseEntity<ResponseApiMessage> acceptFriendRequest(@RequestParam Long userIdx, @RequestParam Long friendIdx) {
        try {
            Friend response = friendService.acceptFriendRequest(userIdx, friendIdx);
            List<Friend> myFriends = response.getUser().getUserFriends();

            return sendResponseHttpByJson(SUCCESS, "친구 요청 수락 성공", myFriends);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 친구 요청 거부/취소/친구 삭제
    @DeleteMapping("")
    public ResponseEntity<ResponseApiMessage> rejectFriendRequest(@RequestParam Long userIdx, @RequestParam Long friendIdx) {
        try {
            Integer friendStatus = friendService.deleteFriendRequest(userIdx, friendIdx);
            List<User> myFriends = friendService.getFriends(userIdx, friendStatus);

            return sendResponseHttpByJson(SUCCESS, "친구 요청 삭제", myFriends);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 받은 친구 요청 목록
    @GetMapping("/receive")
    public ResponseEntity<ResponseApiMessage> getReceivedFriendRequests(@RequestParam Long userIdx) {
        try {
            List<User> requestList = friendService.getFriends(userIdx, 3);

            return sendResponseHttpByJson(SUCCESS, "받은 친구 요청 목록 조회 성공", requestList);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 보낸 친구 요청 목록
    @GetMapping("/send")
    public ResponseEntity<ResponseApiMessage> getSentFriendRequests(@RequestParam Long userIdx) {
        try {
            List<User> requestList = friendService.getFriends(userIdx, 2);

            return sendResponseHttpByJson(SUCCESS, "보낸 친구 요청 목록 조회 성공", requestList);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 친구 상단 고정
    @PatchMapping("fix")
    public ResponseEntity<ResponseApiMessage> updateIsFixed(@RequestParam Long userIdx, @RequestParam Long friendIdx) {
        try {
            friendService.updateIsFixed(userIdx, friendIdx);
            List<User> myFriends = friendService.getFriends(userIdx, 1);

            return sendResponseHttpByJson(SUCCESS, "친구 상단 고정/해제 성공", myFriends);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 친구 플래너 확인
    @GetMapping("friendInfo")
    public ResponseEntity<ResponseApiMessage> getFriendPlanner(@RequestParam Long userIdx, @RequestParam Long friendUserIdx) {
        try {
            User friend = userService.getUser(friendUserIdx);
            if(!friendService.getFriends(userIdx, 1).contains(friend))
                throw new BaseException(EMPTY_FRIEND);

            Badge tier = userBadgeService.getRepresentBadges(friendUserIdx, "tier").get(0).getBadge();

            List<Badge> badges = new ArrayList<>();
            List<UserBadge> userBadges = userBadgeService.getRepresentBadges(userIdx, "badge");
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
