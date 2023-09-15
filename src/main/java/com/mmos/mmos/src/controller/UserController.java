package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.user.UserSaveRequestDto;
import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Planner;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.service.CalendarService;
import com.mmos.mmos.src.service.PlannerService;
import com.mmos.mmos.src.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final static int SUCCESS_CODE = 200;
    private final UserService userService;
    private final CalendarService calendarService;
    private final PlannerService plannerService;

    @ResponseBody
    @PostMapping("")
    public ResponseEntity<ResponseApiMessage> saveUser(@RequestBody UserSaveRequestDto requestDto) {
        // User 생성
        User user = userService.saveUser(requestDto);
        System.out.println("user = " + user.toString());

        // Calendar 생성
        Calendar calendar = calendarService.saveCalendar(LocalDate.now().getMonthValue(), user.getUser_index());
        System.out.println("calendar = " + calendar.toString());

        // Planner 생성
        Planner planner = plannerService.savePlanner(LocalDate.now(), calendar.getCalendar_index());
        System.out.println("planner = " + planner.toString());

        // User, Calendar FK 매핑
        user.addCalendars(calendar);

        // Calendar, Planner FK 매핑
        calendar.addPlanner(planner);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE USER. USER_INDEX=" + user.getUser_index(), requestDto);
    }

}
