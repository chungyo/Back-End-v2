package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.friend.FriendResponseDto;
import com.mmos.mmos.src.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@RestController
@RequestMapping("api/v1/friends")
@RequiredArgsConstructor
public class FriendController extends BaseController {

    private final FriendService friendService;

    /**
     * 친구 요청 보내는 API (완료)
     *      가정: 유저 A가 유저 B에게 친구 요청을 보내는 상황
     *      A의 Friend 객체는 status = 2, User는 B 유저 정보를 담고 있음
     *      B의 Friend 객체는 status = 3, User는 A 유저 정보를 담고 있음
     *      만약 B가 친구 요청을 수락하면 두 객체 모두 status가 1이 됨
     *      거절하면 두 객체 모두 삭제
     * @param userIdx1: 친구 요청을 보내는 유저
     * @param userIdx2: 친구 요청을 받은 유저
     */
    @ResponseBody
    @PostMapping("/{userIdx1}/{userIdx2}")
    public ResponseEntity<ResponseApiMessage> requestFriend(@PathVariable Long userIdx1, @PathVariable Long userIdx2) {
        FriendResponseDto responseDto = friendService.requestFriend(userIdx1, userIdx2);

        if(responseDto.getStatus().equals(INVALID_USER))
            return sendResponseHttpByJson(INVALID_USER, "존재하지 않는 유저입니다.", null);
        if(responseDto.getStatus().equals(FRIEND_COMPLETE_REQUEST))
            return sendResponseHttpByJson(FRIEND_COMPLETE_REQUEST, "이미 처리된 요청입니다.", null);
        return sendResponseHttpByJson(SUCCESS, "친구 추가 요청 완료", responseDto);
    }

    /**
     * 친구 요청 수락하는 API (완료)
     * @param userIdx2: 친구 요청을 받아서 수락하는 userIdx
     * @param userIdx1: 친구 요청을 보내서 응답을 기다리는 userIdx
     */
    // 친구 요청 수락
    @ResponseBody
    @PatchMapping("/{userIdx2}/{userIdx1}")
    public ResponseEntity<ResponseApiMessage> acceptRequest(@PathVariable Long userIdx2, @PathVariable Long userIdx1) {
        FriendResponseDto responseDto = friendService.acceptRequest(userIdx2, userIdx1);

        if(responseDto.getStatus().equals(FRIEND_COMPLETE_REQUEST))
            return sendResponseHttpByJson(FRIEND_COMPLETE_REQUEST, "이미 처리된 요청입니다.", null);
        return sendResponseHttpByJson(SUCCESS, "친구 요청 수락 완료", responseDto);
    }

    /**
     * 친구 요청 거부, 친구 요청 취소, 친구 삭제하는 API
     * @param userIdx2: 삭제하는 주체의 userIdx
     * @param userIdx1: 삭제하고 싶은 친구의 userIdx
     */
    // 친구 요청 거절 and 삭제 or 취소
    @ResponseBody
    @DeleteMapping("/{userIdx2}/{userIdx1}")
    public ResponseEntity<ResponseApiMessage> rejectRequest(@PathVariable Long userIdx2, @PathVariable Long userIdx1) {
        Long idx = friendService.rejectRequest(userIdx2, userIdx1);

        if(idx == null)
            return sendResponseHttpByJson(FRIEND_COMPLETE_REQUEST, "이미 처리된 요청입니다.", null);
        return sendResponseHttpByJson(SUCCESS, "친구 요청 거절 완료", idx);
    }

    /**
     * 친구를 상단에 고정시키는 API (완료)
     * @param userIdx1: 고정 시키는 주체의 userIdx
     * @param userIdx2: 상단에 고정하려는 친구의 userIdx
     */
    // 고정 친구로 변경
    @ResponseBody
    @PatchMapping("/fix/{userIdx1}/{userIdx2}")
    public ResponseEntity<ResponseApiMessage> updateFixedFriend(@PathVariable Long userIdx1, @PathVariable Long userIdx2) {
        FriendResponseDto responseDto = friendService.updateFixedFriend(userIdx1, userIdx2);

        return sendResponseHttpByJson(SUCCESS, "고정 친구 설정 완료", responseDto);
    }

    /**
     * 내 친구 목록 조회 API (완료)
     *      순서: 상단 고정 친구(유저 이름 순) -> 일반 친구(유저 이름 순)
     * @param userIdx:
     * @param pageable
     * @return
     */
    // 내 친구 목록 조회
    @ResponseBody
    @GetMapping("/{userIdx}")
    public ResponseEntity<ResponseApiMessage> getFriends(@PathVariable Long userIdx, @PageableDefault(page = 0, size = 10, sort = "friendIndex", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<FriendResponseDto> page = friendService.getFriends(userIdx, 1,  pageable);

        return sendResponseHttpByJson(SUCCESS, "친구 목록 조회 완료", page);
    }

    /**
     * 내가 보낸 친구 요청들 목록 조회 API (완료)
     * @param userIdx: 내 userIdx
     * @param pageable: 페이징을 위한 기본 파라미터
     */
    // 내가 보낸 친구 요청 조회
    @ResponseBody
    @GetMapping("send/{userIdx}")
    public ResponseEntity<ResponseApiMessage> getSendRequestFriends(@PathVariable Long userIdx, @PageableDefault(page = 0, size = 10, sort = "friendIndex", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<FriendResponseDto> page = friendService.getFriends(userIdx, 2, pageable);

        return sendResponseHttpByJson(SUCCESS, "친구 목록 조회 완료", page);
    }

    /**
     * 내가 받은 친구 요청들 목록 조회 API (완료)
     * @param userIdx: 내 userIdx
     * @param pageable: 페이징을 위한 기본 파라미터
     */
    // 내가 받은 친구 요청 조회
    @ResponseBody
    @GetMapping("receive/{userIdx}")
    public ResponseEntity<ResponseApiMessage> getReceiveRequestFriends(@PathVariable Long userIdx, @PageableDefault(page = 0, size = 10, sort = "friendIndex", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<FriendResponseDto> page = friendService.getFriends(userIdx, 3, pageable);

        return sendResponseHttpByJson(SUCCESS, "친구 목록 조회 완료", page);
    }
}
