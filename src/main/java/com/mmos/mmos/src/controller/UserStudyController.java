package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.userstudy.UserStudyMemberStatusUpdateDto;
import com.mmos.mmos.src.domain.dto.userstudy.UserStudyResponseDto;
import com.mmos.mmos.src.service.UserStudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/userstudies")
@RequiredArgsConstructor
public class UserStudyController extends BaseController {
    private final UserStudyService userStudyService;

    // 유저스터디 생성
    @ResponseBody
    @PostMapping("/{userIdx}/{studyIdx}/save")
    public ResponseEntity<ResponseApiMessage> saveUserStudy(@PathVariable Long userIdx, @PathVariable Long studyIdx) {
        // 유저스터디 생성
        UserStudyResponseDto userStudyResponseDto = userStudyService.saveUserStudy(3, userIdx, studyIdx);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE USER STUDY. USERSTUDY_INDEX=" + userStudyResponseDto.getIndex(), userStudyResponseDto);
    }

    // Userstudy.isMember : 1 == leader, 2 == manager, 3 == member, 4 == application, 5 == invitee
    @ResponseBody
    @PostMapping("/{userIdx}/{studyIdx}/saveApplication")
    public ResponseEntity<ResponseApiMessage> saveUserStudyApplication(@PathVariable Long userIdx, @PathVariable Long studyIdx) {
        // 유저스터디 생성
        UserStudyResponseDto userStudyResponseDto = userStudyService.saveUserStudy(4, userIdx, studyIdx);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE USER STUDY APPLIER. USERSTUDY_INDEX=" + userStudyResponseDto.getIndex(), userStudyResponseDto);
    }

    // Userstudy.isMember : 1 == leader, 2 == manager, 3 == member, 4 == application, 5 == invitee
    @ResponseBody
    @PostMapping("/{userIdx}/{studyIdx}/saveInvitee")
    public ResponseEntity<ResponseApiMessage> saveUserStudyInvitee(@PathVariable Long userIdx, @PathVariable Long studyIdx) {
        // 유저스터디 생성
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
    
    // 리더 위임
    @ResponseBody
    @PatchMapping("/{userStudyIdx}/{newUserStudyIdx}/leaderUpdate")
    public ResponseEntity<ResponseApiMessage> updateLeader(@PathVariable Long userStudyIdx, @PathVariable Long newUserStudyIdx) {
        // 동일 인물 확인
        if (Objects.equals(userStudyIdx, newUserStudyIdx)) return null;
        
        // 리더 업데이트
        UserStudyResponseDto userStudyResponseDto = userStudyService.updateLeader(userStudyIdx, newUserStudyIdx);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE USER STUDY. USERSTUDY_INDEX=" + userStudyResponseDto.getIndex(), userStudyResponseDto);
    }

    // 멤버 지위 변경
    @ResponseBody
    @PatchMapping("/{userStudyIdx}/statusUpdate")
    public ResponseEntity<ResponseApiMessage> updateManager(@PathVariable Long userStudyIdx, @RequestBody UserStudyMemberStatusUpdateDto userStudyMemberStatusUpdateDto) {

        // isMember 업데이트
        UserStudyResponseDto userStudyResponseDto = userStudyService.updateMember(userStudyIdx, userStudyMemberStatusUpdateDto.getMemberStatus());

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE USER STUDY. USERSTUDY_INDEX=" + userStudyResponseDto.getIndex(), userStudyResponseDto);
    }
}