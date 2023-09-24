package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.study.StudyNameUpdateDto;
import com.mmos.mmos.src.domain.dto.study.StudyResponseDto;
import com.mmos.mmos.src.domain.dto.study.StudySaveRequestDto;
import com.mmos.mmos.src.domain.dto.userstudy.UserStudyResponseDto;
import com.mmos.mmos.src.service.StudyService;
import com.mmos.mmos.src.service.UserStudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mmos.mmos.config.HttpResponseStatus.UPDATE_STUDY_EMPTY_NAME;

@RestController
@RequestMapping("/api/v1/studies")
@RequiredArgsConstructor
public class StudyController extends BaseController {
    private final StudyService studyService;
    private final UserStudyService userStudyService;

    // Study 방 생성
    @ResponseBody
    @PostMapping("/save/{userIdx}")
    public ResponseEntity<ResponseApiMessage> saveStudy(@RequestBody StudySaveRequestDto requestDto, @PathVariable Long userIdx) {
        // Study 생성
        StudyResponseDto studyResponseDto = studyService.saveStudy(requestDto);

        // UserStudy 생성 + study -> UserStudy 매핑
        UserStudyResponseDto userStudyResponseDto = userStudyService.saveUserStudy(1, studyResponseDto.getIndex(), userIdx);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE STUDY. STUDY_INDEX=" + studyResponseDto.getIndex(), studyResponseDto);
    }

    // Study 이름 변경
    @ResponseBody
    @PatchMapping("/nameUpdate/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> updateStudyName(@RequestBody StudyNameUpdateDto studyNameUpdateDto, @PathVariable Long studyIdx) {
        // null 검사
        if (studyNameUpdateDto.getNewStudyName() == null)
            return sendResponseHttpByJson(UPDATE_STUDY_EMPTY_NAME, "UPDATE STUDY_NAME FAIL. USER_IDX=" + studyIdx, null);

        // Study 이름 업데이트
        StudyResponseDto studyResponseDto = studyService.updateStudyName(studyIdx, studyNameUpdateDto.getNewStudyName());

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "UPDATE STUDY_NAME. STUDY_INDEX=" + studyIdx, studyResponseDto);
    }

    // Study 완료 처리
    @ResponseBody
    @PatchMapping("/complete/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> updateStudyIsComplete(@PathVariable Long studyIdx){
        // Study 완료 업데이트
        StudyResponseDto studyResponseDto = studyService.updateStudyIsComplete(studyIdx);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "UPDATE STUDY_COMPLETE. STUDY_INDEX=" + studyIdx, studyResponseDto);
    }

}
