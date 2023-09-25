package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.userstudy.UserStudyMemberStatusUpdateDto;
import com.mmos.mmos.src.domain.dto.userstudy.UserStudyResponseDto;
import com.mmos.mmos.src.service.UserStudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/userstudies")
@RequiredArgsConstructor
public class UserStudyController extends BaseController {
    private final UserStudyService userStudyService;

    /**
     * 유저 스터디 생성 (수정 중 - 초대(5), 참가 요청(4)한 유저가 이미 객체로 생성됐다면 patch로 참여한 인원들은 3으로 바꿔주면 될 것 같음)
     * @param userIdx
     * @param studyIdx
     */
    @ResponseBody
    @PostMapping("/save/{userIdx}/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> saveUserStudy(@PathVariable Long userIdx, @PathVariable Long studyIdx) {
        // 멤버 생성 (3 == 일반 멤버)
        UserStudyResponseDto userStudyResponseDto = userStudyService.saveUserStudy(3, userIdx, studyIdx);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE USER STUDY. USERSTUDY_INDEX=" + userStudyResponseDto.getIndex(), userStudyResponseDto);
    }

    /**
     * ERD 수정으로 인한 새로운 로직 필요
     * @param userIdx
     * @param studyIdx
     * @return
     */
    // Userstudy.isMember : 1 == leader, 2 == manager, 3 == member, 4 == application, 5 == invitee
    @ResponseBody
    @PostMapping("/saveApplier/{userIdx}/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> saveUserStudyApplication(@PathVariable Long userIdx, @PathVariable Long studyIdx) {
        // 지원자 생성 (4 == 지원자)
        UserStudyResponseDto userStudyResponseDto = userStudyService.saveUserStudy(4, userIdx, studyIdx);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE USER STUDY APPLIER. USERSTUDY_INDEX=" + userStudyResponseDto.getIndex(), userStudyResponseDto);
    }

    /**
     * ERD 수정으로 인한 새로운 로직 필요
     * @param userIdx
     * @param studyIdx
     * @return
     */
    // Userstudy.isMember : 1 == leader, 2 == manager, 3 == member, 4 == application, 5 == invitee
    @ResponseBody
    @PostMapping("/saveInvitee/{userIdx}/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> saveUserStudyInvitee(@PathVariable Long userIdx, @PathVariable Long studyIdx) {
        // 초대받은 유저 생성 (5 == 초대받은 유저)
        UserStudyResponseDto userStudyResponseDto = userStudyService.saveUserStudy(5, userIdx, studyIdx);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE USER STUDY INVITEE. USERSTUDY_INDEX=" + userStudyResponseDto.getIndex(), userStudyResponseDto);
    }

//    @ResponseBody
//    @GetMapping("/{userIdx}/saveInvitee")
//    public ResponseEntity<ResponseApiMessage> getUserStudy(@PathVariable Long userIdx, @PathVariable Long studyIdx) {
//        // 유저스터디 생성
//        UserStudyResponseDto userStudyResponseDto = userStudyService.saveUserStudy(5, userIdx, studyIdx);
//
//        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE USER STUDY INVITEE. USERSTUDY_INDEX=" + userStudyResponseDto.getIndex(), userStudyResponseDto);
//    }

    /**
     * 수정 중 - 컨트롤러 단에서 return null은 X
     * @param userStudyIdx
     * @param newUserStudyIdx
     * @return
     */
    // 리더 위임
    @ResponseBody
    @PatchMapping("/leaderUpdate/{userStudyIdx}/{newUserStudyIdx}")
    public ResponseEntity<ResponseApiMessage> updateLeader(@PathVariable Long userStudyIdx, @PathVariable Long newUserStudyIdx) {
        // 동일 인물 확인
        if (userStudyIdx.equals(newUserStudyIdx))
            return null;
        
        // 리더 업데이트
        List<UserStudyResponseDto> userStudyResponseDtoList = userStudyService.updateLeader(userStudyIdx, newUserStudyIdx);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "UPDATE USER STUDY.", userStudyResponseDtoList);
    }

    /**
     * 수정 중 - 수정하려는 주체가 리더인지 확인 X
     * @param userStudyIdx
     * @param userStudyMemberStatusUpdateDto
     * @return
     */
    // 멤버 지위 변경
    @ResponseBody
    @PatchMapping("/statusUpdate/{userStudyIdx}")
    public ResponseEntity<ResponseApiMessage> updateMemberStatus(@PathVariable Long userStudyIdx, @RequestBody UserStudyMemberStatusUpdateDto userStudyMemberStatusUpdateDto) {

        // isMember 업데이트
        UserStudyResponseDto userStudyResponseDto = userStudyService.updateMember(userStudyIdx, userStudyMemberStatusUpdateDto.getMemberStatus());

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "UPDATE USER STUDY. USERSTUDY_INDEX=" + userStudyResponseDto.getIndex(), userStudyResponseDto);
    }
}