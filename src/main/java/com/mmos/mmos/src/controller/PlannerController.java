package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.planner.PlannerResponseDto;
import com.mmos.mmos.src.domain.dto.planner.PlannerUpdateMemoRequestDto;
import com.mmos.mmos.src.service.PlanService;
import com.mmos.mmos.src.service.PlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/planners")
@RequiredArgsConstructor
public class PlannerController extends BaseController {

    private final PlannerService plannerService;
    private final PlanService planService;

    /**
     * 플래너 메모 추가 기능 (완료)
     * @param requestDto
     *         - String memo: 메모
     * @param plannerIdx: 플래너 인덱스
     */
    // 플래너 메모 기능
    @ResponseBody
    @PatchMapping("memo/{plannerIdx}")
    public ResponseEntity<ResponseApiMessage> updatePlannerMemo(@RequestBody PlannerUpdateMemoRequestDto requestDto, @PathVariable Long plannerIdx){
         PlannerResponseDto responseDto = plannerService.setMemo(plannerIdx,requestDto.getMemo());

         return sendResponseHttpByJson(SUCCESS, "UPDATE PLANNER_MEMO_COMPLETE. PLANNER_INDEX=" + plannerIdx, responseDto);

    }

    /**
     * 플래너와 관련된 모든 것 가져오는 기능 (계획들, 프로젝트, 메모, 날짜, 총 공부 시간, 총 공부량)
     * @param userIdx: 프로젝트 찾기 위한 유저 인덱스
     * @param plannerIdx: 플래너와 관련된 모든 것들 받아오기 위한 플래너 인덱스
     */
    @ResponseBody
    @GetMapping("/all/{userIdx}/{plannerIdx}")
    public ResponseEntity<ResponseApiMessage> getPlanner(@PathVariable Long userIdx, @PathVariable Long plannerIdx) {
        PlannerResponseDto responseDtoList = plannerService.getPlanner(plannerIdx, userIdx);
        return sendResponseHttpByJson(SUCCESS, "GET PLANNER COMPLETE. PLANNER_INDEX=" + plannerIdx, responseDtoList);
    }


}
