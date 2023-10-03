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
     * @param userIdx: 캘린더를 생성할 유저 인덱스
     */
    @ResponseBody
    @PostMapping("/save/{userIdx}")
    public ResponseEntity<ResponseApiMessage> saveCalendar(@PathVariable Long userIdx) {
        // 오늘이 무슨 달인지 확인
        int thisMonth = LocalDate.now().getMonthValue();
        int thisYear = LocalDate.now().getYear();

        // 저장
        CalendarResponseDto calendarResponseDto= calendarService.saveCalendar(thisYear, thisMonth, userIdx);
        if(calendarResponseDto.getStatus() == INVALID_USER)
            return sendResponseHttpByJson(POST_CALENDAR_INVALID_REQUEST, "Calendar save failed(No User Found). USER_IDX" +userIdx, null);
        if(calendarResponseDto.getStatus() == POST_CALENDAR_INVALID_REQUEST)
            return sendResponseHttpByJson(POST_CALENDAR_INVALID_REQUEST, "Calendar save failed. THIS_DATE" + thisYear +"-"+thisMonth, null);
        return sendResponseHttpByJson(SUCCESS, "Saved calendar THIS_MONTH=" + thisMonth, calendarResponseDto);
    }


    /**
     * 캘린더를 조회하는 API (완료)
     * @param userIdx: 가져올 캘린더를 소유한 유저 인덱스
     * @param calendarGetRequestDto:
     *              Integer year: 가져올 캘린더의 연도
     *              Integer month: 가져올 캘린더의 월
     */
    @ResponseBody
    @GetMapping("/get/{userIdx}")
    public ResponseEntity<ResponseApiMessage> getCalendar(@PathVariable Long userIdx, @RequestBody CalendarGetRequestDto calendarGetRequestDto) {
        // null validation
        if(calendarGetRequestDto.getYear()==null||calendarGetRequestDto.getMonth()==null)
            return sendResponseHttpByJson(GET_CALENDAR_EMPTY_REQUEST, "No Date Selected", null);

        CalendarResponseDto calendarResponseDto = calendarService.getCalendar(userIdx,calendarGetRequestDto);
        if(calendarResponseDto.getStatus().equals(INVALID_USER)){
            return sendResponseHttpByJson(INVALID_USER,"User Not Found. userIdx=" + userIdx, null);
        }
        if(calendarResponseDto.getStatus().equals(GET_CALENDAR_EMPTY_REQUEST)){
            return sendResponseHttpByJson(GET_CALENDAR_EMPTY_REQUEST,"캘린더 날짜를 입력해주세요.", null);
        }
        return sendResponseHttpByJson(SUCCESS, "Got calendar THIS_YEAR=" + calendarGetRequestDto.getYear() + " THIS_MONTH=" + calendarGetRequestDto.getMonth(), calendarResponseDto);
    }
}
