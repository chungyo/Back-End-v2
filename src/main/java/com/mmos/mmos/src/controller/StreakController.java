package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.streak.StreakResponseDto;
import com.mmos.mmos.src.service.StreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/streaks")
@RequiredArgsConstructor
public class StreakController extends BaseController {
    private final StreakService streakService;

    /**
     * 스트릭 저장 (탑 스트릭, 최근 스트릭도 자동 업데이트) API
     * @param userIdx: 스트릭을 저장하려는 유저의 인덱스
     */
    @ResponseBody
    @PostMapping("/{userIdx}")
    public ResponseEntity<ResponseApiMessage> saveStreak(@PathVariable Long userIdx) {
        StreakResponseDto responseDto = streakService.saveStreak(userIdx);

        return sendResponseHttpByJson(SUCCESS, "스트릭 저장 성공", responseDto);
    }

    /**
     * 최근 60일치 스트릭 조회 API
     * @param userIdx: 조회하려는 스트릭의 유저 인덱스
     */
    // 스트릭 조회 (60일치)
    @ResponseBody
    @GetMapping("/{userIdx}")
    public ResponseEntity<ResponseApiMessage> getStreaks(@PathVariable Long userIdx) {
        List<StreakResponseDto> responseDtoList = streakService.getStreaks(userIdx);

        return sendResponseHttpByJson(SUCCESS, "스트릭 조회 성공", responseDtoList);
    }
}
