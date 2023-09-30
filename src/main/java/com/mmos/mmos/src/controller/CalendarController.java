package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.calendar.CalendarGetRequestDto;
import com.mmos.mmos.src.domain.dto.calendar.CalendarResponseDto;
import com.mmos.mmos.src.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.mmos.mmos.config.HttpResponseStatus.*;

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
    @PostMapping("/save/{userIdx}")
    public ResponseEntity<ResponseApiMessage> saveCalendar(@PathVariable Long userIdx) {
        // 오늘이 무슨 달인지 확인
        int thisMonth = LocalDate.now().getMonthValue();
        int thisYear = LocalDate.now().getYear();

        // 저장
        if(calendarService.saveCalendar(thisYear, thisMonth, userIdx).getStatus() == null)
            return sendResponseHttpByJson(POST_CALENDAR_INVALID_REQUEST, "Calendar save failed. THIS_MONTH=" + thisMonth, null);
        return sendResponseHttpByJson(SUCCESS, "Saved calendar THIS_MONTH=" + thisMonth, null);
    }


    @ResponseBody
    @GetMapping("/get/{userIdx}")
    public ResponseEntity<ResponseApiMessage> getCalendar(@PathVariable Long userIdx, @RequestBody CalendarGetRequestDto calendarGetRequestDto) {
        // null validation
        if(calendarGetRequestDto.getYear()==null||calendarGetRequestDto.getMonth()==null)
            return sendResponseHttpByJson(GET_CALENDAR_EMPTY_REQUEST, "No Date Selected", null);

        CalendarResponseDto calendarResponseDto = calendarService.getCalendar(userIdx,calendarGetRequestDto);

        return sendResponseHttpByJson(SUCCESS, "Got calendar THIS_YEAR=" + calendarGetRequestDto.getYear() + " THIS_MONTH=" + calendarGetRequestDto.getMonth(), calendarResponseDto);
    }
}
