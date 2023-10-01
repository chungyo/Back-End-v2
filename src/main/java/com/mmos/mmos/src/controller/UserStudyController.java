package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.userstudy.UserStudyAttendDto;
import com.mmos.mmos.src.domain.dto.userstudy.UserStudyInviteDto;
import com.mmos.mmos.src.domain.dto.userstudy.UserStudyResponseDto;
import com.mmos.mmos.src.service.UserStudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@RestController
@RequestMapping("/api/v1/userstudies")
@RequiredArgsConstructor
public class UserStudyController extends BaseController {
    private final UserStudyService userStudyService;

    /**
     * 스터디원 초대하는 API (완료)
     *      스터디원 중 운영진 이상만 초대 보낼 수 있음
     *      파라미터가 userStudyIdx인 이유는 리더가 스터디 페이지 들어가서 초대를 보낼 것이므로 페이지는 리더의 userStudy 정보를 담고있음
     * @param userStudyIdx: 리더의 userStudy
     * @param requestDto
     *          - userIdx: 초대 받은 유저 인덱스
     */
    // 초대 보내기 (객체 생성 - isMember = 4)
    @ResponseBody
    @PostMapping("/invitation/{userStudyIdx}")
    public ResponseEntity<ResponseApiMessage> inviteStudy(@PathVariable Long userStudyIdx, @RequestBody UserStudyInviteDto requestDto) {
        UserStudyResponseDto userStudyResponseDto = userStudyService.inviteStudy(userStudyIdx, requestDto);

        if(userStudyResponseDto.getStatus().equals(USERSTUDY_COMPLETE_REQUEST))
            return sendResponseHttpByJson(USERSTUDY_COMPLETE_REQUEST, "이미 처리된 요청입니다.", null);
        else if(userStudyResponseDto.getStatus().equals(USERSTUDY_MEMBER_ALREADY_EXIST))
            return sendResponseHttpByJson(USERSTUDY_MEMBER_ALREADY_EXIST, "이미 존재하는 유저입니다.", null);
        else if (userStudyResponseDto.getStatus().equals(USERSTUDY_INVALID_REQUEST))
            return sendResponseHttpByJson(USERSTUDY_INVALID_REQUEST, "권한이 없습니다.", null);
        else if (userStudyResponseDto.getStatus().equals(USERSTUDY_MEMBER_LIMIT_FULL))
            return sendResponseHttpByJson(USERSTUDY_MEMBER_LIMIT_FULL, "이미 가득 찬 스터디입니다.", null);
        return sendResponseHttpByJson(SUCCESS, "스터디에 유저 초대 성공.", userStudyResponseDto);
    }

    /**
     * 초대 받은 유저가 스터디 초대를 승낙하는 API (완료)
     *      받은 초대 목록 창에서 '수락'을 클릭했을 때 상호작용 할 API
     * @param studyIdx: 스터디 인덱스
     * @param userIdx: 초대 받은 유저 인덱스
     */
    // 초대 보내기 -> 수락
    @ResponseBody
    @PatchMapping("/invitation/{studyIdx}/{userIdx}")
    public ResponseEntity<ResponseApiMessage> acceptInvite(@PathVariable Long studyIdx, @PathVariable Long userIdx) {
        UserStudyResponseDto userStudyResponseDto = userStudyService.acceptInvite(studyIdx, userIdx);

        if(userStudyResponseDto.getStatus().equals(USERSTUDY_COMPLETE_REQUEST))
            return sendResponseHttpByJson(USERSTUDY_COMPLETE_REQUEST, "이미 처리된 요청입니다.", null);
        if(userStudyResponseDto.getStatus().equals(USERSTUDY_MEMBER_LIMIT_FULL))
            return sendResponseHttpByJson(USERSTUDY_MEMBER_LIMIT_FULL, "이미 가득 찬 스터디입니다.", null);
        return sendResponseHttpByJson(SUCCESS, "Accept Invite.", userStudyResponseDto);
    }

    /**
     * 초대 받은 유저가 스터디 초대를 거절하는 API (완료)
     *      받은 초대 목록 창에서 '거절'을 클릭했을 때 상호작용 할 API
     * @param studyIdx: 스터디 인덱스
     * @param userIdx: 초대 받은 유저 인덱스
     */
    // 초대 보내기 -> 거절
    @ResponseBody
    @DeleteMapping("/invitation/{studyIdx}/{userIdx}")
    public ResponseEntity<ResponseApiMessage> rejectInvite(@PathVariable Long studyIdx, @PathVariable Long userIdx) {
        Long userStudyIdx = userStudyService.rejectInvite(studyIdx, userIdx);

        if(userStudyIdx == null)
            return sendResponseHttpByJson(USERSTUDY_COMPLETE_REQUEST, "이미 처리된 요청입니다.", null);
        return sendResponseHttpByJson(SUCCESS, "Reject Invite.", userStudyIdx);
    }

    /**
     * 운영진 이상의 멤버가 유저에게 보낸 초대를 철회하는 API (완료)
     * @param userStudyIdx: 운영진 userStudyIdx
     * @param requestDto
     *          - userIdx: 초대 보낸 유저 인덱스
     */
    // 초대 철회 -> 임원 이상의 직책이 가진
    @ResponseBody
    @DeleteMapping("/invitation/{userStudyIdx}")
    public ResponseEntity<ResponseApiMessage> cancelInvite(@PathVariable Long userStudyIdx, @RequestBody UserStudyInviteDto requestDto) {
        Long idx = userStudyService.cancelInvite(userStudyIdx, requestDto);

        if(idx == -2L)
            return sendResponseHttpByJson(USERSTUDY_COMPLETE_REQUEST, "이미 처리된 요청입니다.", null);
        else if (idx == -1L)
            return sendResponseHttpByJson(USERSTUDY_INVALID_REQUEST, "권한이 없습니다.", null);
        else
            return sendResponseHttpByJson(SUCCESS, "Reject Invite.", userStudyIdx);
    }

    /**
     * 유저가 스터디에 참여 요청을 보내는 API (완료)
     * @param userIdx: 참가 요청 보낸 유저 인덱스
     * @param requestDto
     *          - Long studyIdx: 참여하려는 스터디 인덱스
     */
    // 참가 요청
    @ResponseBody
    @PostMapping("/attendance/{userIdx}")
    public ResponseEntity<ResponseApiMessage> attendRequest(@PathVariable Long userIdx, @RequestBody UserStudyAttendDto requestDto) {
        UserStudyResponseDto userStudyResponseDto = userStudyService.attendRequest(userIdx, requestDto);

        if(userStudyResponseDto.getStatus().equals(USERSTUDY_COMPLETE_REQUEST))
            return sendResponseHttpByJson(USERSTUDY_COMPLETE_REQUEST, "이미 처리된 요청입니다.", null);
        else if(userStudyResponseDto.getStatus().equals(USERSTUDY_ALREADY_EXIST))
            return sendResponseHttpByJson(USERSTUDY_ALREADY_EXIST, "이미 참가한 스터디입니다.", null);
        else if (userStudyResponseDto.getStatus().equals(USERSTUDY_MEMBER_LIMIT_FULL))
            return sendResponseHttpByJson(USERSTUDY_ALREADY_EXIST, "이미 가득 찬 스터디입니다.", null);
        return sendResponseHttpByJson(SUCCESS, "참가 요청 완료", userStudyResponseDto);
    }

    /**
     * 받은 스터디 참여 요청을 수락하는 API (완료)
     * @param userStudyIdx1: 운영진 userStudyIdx
     * @param userStudyIdx2: 스터디 참여 요청을 보낸 userStudyIdx
     */
    // 참가 요청 -> 수락(임원만 가능)
    @ResponseBody
    @PatchMapping("/attendance/{userStudyIdx1}/{userStudyIdx2}")
    public ResponseEntity<ResponseApiMessage> acceptAttend(@PathVariable Long userStudyIdx1, @PathVariable Long userStudyIdx2) {
        UserStudyResponseDto userStudyResponseDto = userStudyService.acceptAttend(userStudyIdx1, userStudyIdx2);

        if(userStudyResponseDto.getStatus().equals(USERSTUDY_COMPLETE_REQUEST))
            return sendResponseHttpByJson(USERSTUDY_COMPLETE_REQUEST, "이미 처리된 요청입니다.", null);
        else if(userStudyResponseDto.getStatus().equals(USERSTUDY_INVALID_REQUEST))
            return sendResponseHttpByJson(USERSTUDY_INVALID_REQUEST, "권한이 없습니다.", null);
        else if(userStudyResponseDto.getStatus().equals(USERSTUDY_MEMBER_LIMIT_FULL))
            return sendResponseHttpByJson(USERSTUDY_MEMBER_LIMIT_FULL, "이미 가득 찬 스터디입니다.", null);
        return sendResponseHttpByJson(SUCCESS, "참가 요청 수락", userStudyResponseDto);
    }

    /**
     * 받은 스터디 참가 요청을 운영진이 거절하는 API (완료)
     * @param userStudyIdx1: 운영진 userStudyIdx
     * @param userStudyIdx2: 참가 요청을 보낸 userStudyIdx
     */
    // 참가 요청 -> 거절(임원만 가능)
    @ResponseBody
    @DeleteMapping("/attendance/{userStudyIdx1}/{userStudyIdx2}")
    public ResponseEntity<ResponseApiMessage> rejectAttend(@PathVariable Long userStudyIdx1, @PathVariable Long userStudyIdx2) {
        Long userStudyIdx = userStudyService.rejectAttend(userStudyIdx1, userStudyIdx2);

        if(userStudyIdx == -1L)
            return sendResponseHttpByJson(USERSTUDY_INVALID_REQUEST, "권한이 없습니다.", null);
        if(userStudyIdx == -2L)
            return sendResponseHttpByJson(USERSTUDY_COMPLETE_REQUEST, "이미 처리된 요청입니다.", null);
        return sendResponseHttpByJson(SUCCESS, "참가 요청 거절 완료", userStudyIdx);

    }

    // 참가 철회
    @ResponseBody
    @DeleteMapping("/attendance/{userStudyIdx}")
    ResponseEntity<ResponseApiMessage> cancelAttend(@PathVariable Long userStudyIdx) {
        Long idx = userStudyService.cancelAttend(userStudyIdx);

        if(idx == null)
            return sendResponseHttpByJson(USERSTUDY_COMPLETE_REQUEST, "이미 처리된 요청입니다.", null);
        return sendResponseHttpByJson(SUCCESS, "참가 요청 철회 완료", idx);
    }

    // 탈퇴

    /**
     * 스터디 탈퇴하는 API (완료)
     * 스터디 장은 탈퇴가 불가능 하도록,
     * 만약 스터디 장이 탈퇴하려고 하면 팝업창으로 스터디 장 위임 후 진행해달라고 하고 금지
     * @param userStudyIdx: 탈퇴하려는 userStudyIdx
     */
    @ResponseBody
    @DeleteMapping("/leave/{userStudyIdx}")
    ResponseEntity<ResponseApiMessage> leaveStudy(@PathVariable Long userStudyIdx) {
        Long idx = userStudyService.leaveStudy(userStudyIdx);

        if(idx == null)
            return sendResponseHttpByJson(USERSTUDY_COMPLETE_REQUEST, "이미 처리된 요청입니다.", null);
        return sendResponseHttpByJson(SUCCESS, "스터디 탈퇴 완료", idx);
    }

    /**
     * 스터디 장이 일반 멤버 또는 운영진을 추방하는 API (완료)
     *      스터디 장만이 추방할 수 있음
     *              스터디 운영진이 가능한 권한
     *                  1. 홍보게시글 업로드, 수정, 삭제
     *                  2. 공지게시글 업로드, 수정, 삭제
     *                  3. 참가 요청 수락, 거절
     *                  4. 스터디 초대
     * @param userStudyIdx1: 스터디 장 userStudyIdx
     * @param userStudyIdx2: 추방하려는 멤버 userStudyIdx
     */
    // 추방
    @ResponseBody
    @DeleteMapping("/kick/{userStudyIdx1}/{userStudyIdx2}")
    ResponseEntity<ResponseApiMessage> kickMember(@PathVariable Long userStudyIdx1, @PathVariable Long userStudyIdx2) {
        Long idx = userStudyService.kickMember(userStudyIdx1, userStudyIdx2);

        if(idx == -1L)
            return sendResponseHttpByJson(USERSTUDY_INVALID_REQUEST, "권한이 없습니다.", null);
        if(idx == -2L)
            return sendResponseHttpByJson(USERSTUDY_COMPLETE_REQUEST, "이미 처리된 요청입니다.", null);
        return sendResponseHttpByJson(SUCCESS, "스터디 추방 완료", idx);
    }

    /**
     * 직책 변경 API
     *      직책 변경은 스터디원 목록 옆에 '변경' 버튼을 누른 다음
     *      원하는 직책 중 하나를 선택하고 '변경하기' 버튼을 누르고 '정말 변경하시겠습니까?' 문구를 확인버튼 누르면 변경
     *      선택지는 스터디 장, 운영진, 멤버 3개 존재
     * 운영진
     *      멤버 -> 운영진 변경 가능
     * 스터디 장
     *      멤버 -> 운영진
     *      운영진 or 멤버 -> 스터디 장
     *      운영진 -> 멤버 변경 가능
     *
     * @param userStudyIdx1: 운영진 userStudyIdx
     * @param userStudyIdx2: 권한을 수정하려는 userStudyIdx
     */
    // 운영진 직책 변경 (운영진만이 가능, but 스터디 장 위임은 스터디 장만 가능)
    @ResponseBody
    @PatchMapping("/{userStudyIdx1}/{userStudyIdx2}")
    ResponseEntity<ResponseApiMessage> updatePosition(@PathVariable Long userStudyIdx1, @PathVariable Long userStudyIdx2, @RequestParam Long position) {
        UserStudyResponseDto responseDto = userStudyService.updatePosition(userStudyIdx1, userStudyIdx2, position);

        if(responseDto.getStatus().equals(USERSTUDY_INVALID_REQUEST))
            return sendResponseHttpByJson(USERSTUDY_INVALID_REQUEST, "권한이 없습니다.", null);
        return sendResponseHttpByJson(SUCCESS, "직책 변경 완료", responseDto);
    }
}