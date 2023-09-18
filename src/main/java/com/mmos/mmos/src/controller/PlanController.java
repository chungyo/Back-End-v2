package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.plan.PlanSaveRequestDto;
import com.mmos.mmos.src.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
public class PlanController extends BaseController {

    private final PlanService planService;

    @ResponseBody
    @PostMapping("/{plannerIdx}")
    public ResponseEntity<ResponseApiMessage> savePlan(@PathVariable Long plannerIdx, @RequestBody PlanSaveRequestDto requestDto) {
        planService.savePlan(requestDto, plannerIdx);

        return sendResponseHttpByJson(SUCCESS, "Saved Plan. PLANNER_INDEX=" + plannerIdx, requestDto);
    }
}
