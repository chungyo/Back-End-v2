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

    /*
        시작 시간 측정 및 마감 시간 측정은 당일의 일정에만 가능하도록 프론트에서 작업을 해야 할 것 같습니다.
     */

    /**
     * 계획 시작 시간 설정하는 API (완료)
     * 계획 시작 시간 측정 버튼 눌렀을 때 보내질 쿼리
     * @param planIdx: 계획 인덱스
     */
    @ResponseBody
    @PostMapping("/{planIdx}")
    public ResponseEntity<ResponseApiMessage> setStartTime(@PathVariable Long planIdx) {
        StudyTimeResponseDto responseDto = studyTimeService.setStartTime(planIdx);

        if(responseDto == null)
            return sendResponseHttpByJson(POST_STUDYTIME_DUPLICATE_REQUEST, "Fail Set Start Study Time.", planIdx);
        return sendResponseHttpByJson(SUCCESS, "Save Start Study Time.", responseDto);
    }

    /**
     * 계획 마감 시간 설정하는 API (수정 중)
     *      1. 의미없는 공부 시간 객체 삭제 (시작/정지 연타로 만들어진 객체 삭제)
     * 시간 측정을 일시정지하는 버튼을 눌렀을 때 보내질 쿼리
     * @param planIdx: 계획 인덱스
     */
    @ResponseBody
    @PatchMapping("/{planIdx}")
    public ResponseEntity<ResponseApiMessage> setEndTime(@PathVariable Long planIdx) {
        StudyTimeResponseDto responseDto = studyTimeService.setEndTime(planIdx);

        if(responseDto == null)
            return sendResponseHttpByJson(POST_STUDYTIME_INVALID_REQUEST, "Fail Set End Study Time.", planIdx);
        return sendResponseHttpByJson(SUCCESS, "Save Start Study Time.", responseDto);
    }
}
