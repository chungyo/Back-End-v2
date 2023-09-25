package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.mmos.mmos.config.HttpResponseStatus.POST_CALENDAR_INVALID_REQUEST;
import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/calendars")
@RequiredArgsConstructor
public class CalendarController extends BaseController {

    private final CalendarService calendarService;

    /**
     * 회원가입하거나 한 달이 지나면 새 캘린더가 생성되는 API (완료)
     * @param userIdx: 유저 인덱스
     */
    // 한 달이 지나면 새 캘린더가 생성되도록 하는 컨트롤러
    @ResponseBody
    @PostMapping("/{userIdx}")
    public ResponseEntity<ResponseApiMessage> saveCalendar(@PathVariable Long userIdx) {
        // 오늘이 무슨 달인지 확인
        int thisMonth = LocalDate.now().getMonthValue();

        // 저장
        if(calendarService.saveCalendar(thisMonth, userIdx) == null)
            return sendResponseHttpByJson(POST_CALENDAR_INVALID_REQUEST, "Calendar save failed. THIS_MONTH=" + thisMonth, null);
        return sendResponseHttpByJson(SUCCESS, "Saved calendar THIS_MONTH=" + thisMonth, null);
    }
}
