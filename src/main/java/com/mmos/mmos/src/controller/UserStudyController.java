package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.config.ResponseApiMessage;
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

    @ResponseBody
    @PostMapping("/{userIdx}/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> saveUserStudy(@PathVariable Long studyIdx, @PathVariable Long userIdx) {
        // 유저스터디 생성
        UserStudyResponseDto userStudyResponseDto = userStudyService.saveUserStudy(false, userIdx, studyIdx);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE USER STUDY. USERSTUDY_INDEX=" + userStudyResponseDto.getIndex(), userStudyResponseDto);
    }

    
    // 리더 위임
    @ResponseBody
    @PatchMapping("/{userStudyIdx}/{newUserStudyIdx}")
    public ResponseEntity<ResponseApiMessage> updateLeader(@PathVariable Long userStudyIdx, @PathVariable Long newUserStudyIdx) {
        // 동일 인물 확인
        if (Objects.equals(userStudyIdx, newUserStudyIdx)) return null;
        
        // 리더 업데이트
        UserStudyResponseDto userStudyResponseDto = userStudyService.updateLeader(userStudyIdx, newUserStudyIdx);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE USER STUDY. USERSTUDY_INDEX=" + userStudyResponseDto.getIndex(), userStudyResponseDto);
    }
}