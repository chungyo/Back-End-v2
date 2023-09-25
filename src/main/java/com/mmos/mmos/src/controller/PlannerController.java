package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.planner.PlannerResponseDto;
import com.mmos.mmos.src.domain.dto.planner.PlannerUpdateMemoRequestDto;
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

    /**
     * 플래너 메모 추가 기능 (완료)
     * @param requestDto
     *         - String memo: 메모
     * @param plannerIdx: 플래너 인덱스
     */
    // 플래너 메모 기능
    @ResponseBody
    @PatchMapping("/{plannerIdx}")
    public ResponseEntity<ResponseApiMessage> updatePlannerMemo(@RequestBody PlannerUpdateMemoRequestDto requestDto, @PathVariable Long plannerIdx){
         PlannerResponseDto responseDto = plannerService.setMemo(plannerIdx,requestDto.getMemo());

         return sendResponseHttpByJson(SUCCESS, "UPDATE PLANNER_MEMO_COMPLETE. PLANNER_INDEX=" + plannerIdx, responseDto);

    }

}
