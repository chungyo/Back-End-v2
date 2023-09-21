package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.studytime.StudyTimeResponseDto;
import com.mmos.mmos.src.service.StudyTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@RestController
@RequestMapping("/api/v1/studytimes")
@RequiredArgsConstructor
public class StudyTimeController extends BaseController {

    private final StudyTimeService studyTimeService;

    @ResponseBody
    @PostMapping("/{planIdx}")
    public ResponseEntity<ResponseApiMessage> setStartTime(@PathVariable Long planIdx) {
        StudyTimeResponseDto responseDto = studyTimeService.setStartTime(planIdx);

        if(responseDto == null)
            return sendResponseHttpByJson(POST_STUDYTIME_DUPLICATE_REQUEST, "Fail Set Start Study Time.", planIdx);
        return sendResponseHttpByJson(SUCCESS, "Save Start Study Time.", responseDto);
    }

    @ResponseBody
    @PatchMapping("/{planIdx}")
    public ResponseEntity<ResponseApiMessage> setEndTime(@PathVariable Long planIdx) {
        StudyTimeResponseDto responseDto = studyTimeService.setEndTime(planIdx);

        if(responseDto == null)
            return sendResponseHttpByJson(POST_STUDYTIME_INVALID_REQUEST, "Fail Set End Study Time.", planIdx);
        return sendResponseHttpByJson(SUCCESS, "Save Start Study Time.", responseDto);
    }
}
