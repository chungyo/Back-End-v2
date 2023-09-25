package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
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
    @PostMapping("/{userStudyIdx}")
    public ResponseEntity<ResponseApiMessage> inviteStudy(@PathVariable Long userStudyIdx, @RequestBody UserStudyInviteDto requestDto) {
        UserStudyResponseDto userStudyResponseDto = userStudyService.inviteStudy(userStudyIdx, requestDto);

        if(userStudyResponseDto == null)
            return sendResponseHttpByJson(POST_USERSTUDY_DUPLICATE_REQUEST, "이미 보낸 요청입니다.", null);
        else if(userStudyResponseDto.getMemberStatus() <= 3)
            return sendResponseHttpByJson(POST_USERSTUDY_ALREADY_EXIST, "이미 존재하는 유저입니다.", null);
        else if (userStudyResponseDto.getIndex().equals(userStudyIdx))
            return sendResponseHttpByJson(POST_USERSTUDY_INVALID_REQUEST, "권한이 없습니다.", null);
        else
            return sendResponseHttpByJson(SUCCESS, "Invite User.", userStudyResponseDto);
    }

    /**
     * 초대 받은 유저가 스터디 초대를 승낙하는 API (완료)
     *      받은 초대 목록 창에서 '수락'을 클릭했을 때 상호작용 할 API
     * @param studyIdx: 스터디 인덱스
     * @param userIdx: 초대 받은 유저 인덱스
     */
    // 초대 보내기 -> 수락
    @ResponseBody
    @PatchMapping("/{studyIdx}/{userIdx}")
    public ResponseEntity<ResponseApiMessage> acceptInvite(@PathVariable Long studyIdx, @PathVariable Long userIdx) {
        UserStudyResponseDto userStudyResponseDto = userStudyService.acceptInvite(studyIdx, userIdx);

        if(userStudyResponseDto == null)
            return sendResponseHttpByJson(POST_USERSTUDY_COMPLETE_REQUEST, "이미 처리된 요청입니다.", null);
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
    @DeleteMapping("/{studyIdx}/{userIdx}")
    public ResponseEntity<ResponseApiMessage> rejectInvite(@PathVariable Long studyIdx, @PathVariable Long userIdx) {
        Long userStudyIdx = userStudyService.rejectInvite(studyIdx, userIdx);

        if(userStudyIdx == null)
            return sendResponseHttpByJson(POST_USERSTUDY_COMPLETE_REQUEST, "이미 처리된 요청입니다.", null);
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
    @DeleteMapping("/{userStudyIdx}")
    public ResponseEntity<ResponseApiMessage> cancelInvite(@PathVariable Long userStudyIdx, @RequestBody UserStudyInviteDto requestDto) {
        Long idx = userStudyService.cancelInvite(userStudyIdx, requestDto);

        if(idx == -2L)
            return sendResponseHttpByJson(POST_USERSTUDY_COMPLETE_REQUEST, "이미 처리된 요청입니다.", null);
        else if (idx == -1L)
            return sendResponseHttpByJson(POST_USERSTUDY_INVALID_REQUEST, "권한이 없습니다.", null);
        else
            return sendResponseHttpByJson(SUCCESS, "Reject Invite.", userStudyIdx);
    }

    // 참가 요청

    // 참가 요청 -> 수락

    // 참가 요청 -> 거절


}