package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.service.PlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.mmos.mmos.config.HttpResponseStatus.POST_PLANNER_INVALID_REQUEST;
import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/planners")
@RequiredArgsConstructor
public class PlannerController extends BaseController {

    private final PlannerService plannerService;

    // 하루가 지나면 새 플래너 생성되도록 하는 컨트롤러
    @ResponseBody
    @PostMapping("/{calendarIdx}")
    public ResponseEntity<ResponseApiMessage> savePlanner(@PathVariable Long calendarIdx) {
        // 오늘이 무슨 날인지 확인
        LocalDate thisDay = LocalDate.now();

        // 저장
        if(plannerService.savePlanner(thisDay, calendarIdx) == null)
            return sendResponseHttpByJson(POST_PLANNER_INVALID_REQUEST, "Planner save failed. THIS_Day=" + thisDay, null);
        return sendResponseHttpByJson(SUCCESS, "Saved planner THIS_Day=" + thisDay, null);
    }
}
