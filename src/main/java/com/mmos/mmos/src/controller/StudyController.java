package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.study.StudySaveRequestDto;
import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/studies")
@RequiredArgsConstructor
public class StudyController extends BaseController{
    private final StudyService studyService;
    @ResponseBody
    @PostMapping("")
    public ResponseEntity<ResponseApiMessage> saveStudy(@RequestBody StudySaveRequestDto requestDto) {
        // User 생성
        Study study = studyService.saveStudy(requestDto);
        System.out.println("study = " + study.toString());

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE STUDY. STUDY_INDEX=" + study.getStudy_index(), requestDto);
    }

    @PatchMapping("/{studyIdx}/{studyName}")
    public ResponseEntity<ResponseApiMessage> updateStudyName(@PathVariable Long studyIdx, @PathVariable String studyName) {
        studyService.updateStudyName(studyIdx,studyName);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE STUDY. STUDY_INDEX=" + studyIdx, studyName);
    }
}
