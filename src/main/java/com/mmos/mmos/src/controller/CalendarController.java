package com.mmos.mmos.src.controller;

import com.mmos.mmos.src.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/calendars")
@RequiredArgsConstructor
public class CalendarController extends BaseController {

    private final static int SUCCESS_CODE = 200;
    private final CalendarService calendarService;



}
