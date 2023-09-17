package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.study.StudySaveRequestDto;
import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.domain.entity.UserStudy;
import com.mmos.mmos.src.service.StudyService;
import com.mmos.mmos.src.service.UserStudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/studies")
@RequiredArgsConstructor
public class StudyController extends BaseController{
    private final StudyService studyService;
    private final UserStudyService userStudyService;

    // Study 방 생성
    @ResponseBody
    @PostMapping("/{userIdx}")
    public ResponseEntity<ResponseApiMessage> saveStudy(@RequestBody StudySaveRequestDto requestDto, @PathVariable Long userIdx) {
        // Study 생성
        Study study = studyService.saveStudy(requestDto);

        // UserStudy 생성 + study -> UserStudy 매핑
        UserStudy userStudy = userStudyService.saveUserStudy(true,study.getStudy_index(), userIdx);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE STUDY. STUDY_INDEX=" + study.getStudy_index(), requestDto);
    }

    @PatchMapping("/{studyIdx}/{studyName}")
    public ResponseEntity<ResponseApiMessage> updateStudyName(@PathVariable Long studyIdx, @PathVariable String studyName) {
        studyService.updateStudyName(studyIdx,studyName);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "UPDATE STUDY_NAME. STUDY_INDEX=" + studyIdx, studyName);
    }

    @PatchMapping("/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> updateStudyStatus(@PathVariable Long studyIdx) {
        studyService.updateStudyStatus(studyIdx);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "UPDATE STUDY_STATUS. STUDY_INDEX=" + studyIdx, null);
    }
}
