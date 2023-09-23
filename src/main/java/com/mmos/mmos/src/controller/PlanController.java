package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.plan.PlanNameUpdateRequestDto;
import com.mmos.mmos.src.domain.dto.plan.PlanResponseDto;
import com.mmos.mmos.src.domain.dto.plan.PlanSaveRequestDto;
import com.mmos.mmos.src.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.POST_PLAN_INVALID_REQUEST;
import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
public class PlanController extends BaseController {

    private final PlanService planService;

    // 계획 생성
    @ResponseBody
    @PostMapping("/{plannerIdx}")
    public ResponseEntity<ResponseApiMessage> savePlan(@PathVariable Long plannerIdx, @RequestBody PlanSaveRequestDto requestDto) {
        // 계획에 내용이 없을 시 발동되는 조건.
        if(requestDto.getPlanName() == null){
            return sendResponseHttpByJson(POST_PLAN_INVALID_REQUEST, "EMPTY PLAN_NAME.", requestDto);
        }
        planService.savePlan(requestDto, plannerIdx);

        return sendResponseHttpByJson(SUCCESS, "Saved Plan. PLANNER_INDEX=" + plannerIdx, requestDto);
    }

    // 계획 조회 (단일)
    @ResponseBody
    @GetMapping("/{planIdx}")
    public ResponseEntity<ResponseApiMessage> getPlan(@PathVariable Long planIdx) {
        PlanResponseDto responseDto = planService.getPlan(planIdx);

        return sendResponseHttpByJson(SUCCESS, "GET PLAN. PLAN_INDEX = " + planIdx, responseDto);
    }

    // 계획 조회(조건부)
    // 스터디 관련 계획이면 true, 그렇지 않으면 false
    @ResponseBody
    @GetMapping("/all/{planIsStudy}")
    public ResponseEntity<ResponseApiMessage> getPlansByIsStudy(@PathVariable Boolean planIsStudy){
        List<PlanResponseDto> responseDtoList = planService.getPlansByPlanIsStudy(planIsStudy);

        return sendResponseHttpByJson(SUCCESS, "Load Plans.", responseDtoList);
    }

    // 계획 조회 (전체)
    @ResponseBody
    @GetMapping("/all")
    public ResponseEntity<ResponseApiMessage> getPlans() {
        List<PlanResponseDto> responseDtoList = planService.getPlans();

        return sendResponseHttpByJson(SUCCESS, "GET PLANS.", responseDtoList);
    }

    // 계획 내용 수정
    @ResponseBody
    @PatchMapping("/{planIdx}")
    public ResponseEntity<ResponseApiMessage> updatePlan(@PathVariable Long planIdx, @RequestBody PlanNameUpdateRequestDto requestDto) {
        PlanResponseDto responseDto = planService.updatePlan(planIdx, requestDto);

        return sendResponseHttpByJson(SUCCESS, "Update Plan. PLAN_INDEX=" + planIdx, responseDto);
    }
}
