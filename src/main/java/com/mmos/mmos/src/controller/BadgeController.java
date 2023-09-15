package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.badge.BadgeResponseDto;
import com.mmos.mmos.src.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/badges")
@RequiredArgsConstructor
public class BadgeController extends BaseController {

    private final static int SUCCESS_CODE = 200;
    private final BadgeService badgeService;

    @ResponseBody
    @GetMapping("/{badgeIdx}")
    public ResponseEntity<ResponseApiMessage> getBadge(@PathVariable Long badgeIdx) {
        BadgeResponseDto responseDto = badgeService.findById(badgeIdx);

        return sendResponseHttpByJson(SUCCESS, "Load Badge. BADGE_INDEX=" + badgeIdx, responseDto);
    }
}
