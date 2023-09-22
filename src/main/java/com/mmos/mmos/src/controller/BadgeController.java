package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.badge.BadgeResponseDto;
import com.mmos.mmos.src.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/badges")
@RequiredArgsConstructor
public class BadgeController extends BaseController {

    private final BadgeService badgeService;


    // 도전과제, 티어, 프사 나누기
    @ResponseBody
    @GetMapping("/{badgeIdx}")
    public ResponseEntity<ResponseApiMessage> getBadge(@PathVariable Long badgeIdx) {
        BadgeResponseDto responseDto = badgeService.getBadge(badgeIdx);

        return sendResponseHttpByJson(SUCCESS, "Load Badge. BADGE_INDEX=" + badgeIdx, responseDto);
    }

    @ResponseBody
    @GetMapping("/all/{purpose}")
    public ResponseEntity<ResponseApiMessage> getBadgesByPurpose(@PathVariable String purpose) {
        List<BadgeResponseDto> responseDtoList = badgeService.getBadgesByPurpose(purpose);

        return sendResponseHttpByJson(SUCCESS, "Load Badges.", responseDtoList);
    }

}