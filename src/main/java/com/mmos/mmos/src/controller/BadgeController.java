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

    /**
     * 도전과제/티어/프사 인덱스로 조회하는 API
     * @param badgeIdx (도전과제/티어/프사 인덱스)
     */
    // 도전과제, 티어, 프사 나누기
    @ResponseBody
    @GetMapping("/{badgeIdx}")
    public ResponseEntity<ResponseApiMessage> getBadge(@PathVariable Long badgeIdx) {
        BadgeResponseDto responseDto = badgeService.getBadge(badgeIdx);

        return sendResponseHttpByJson(SUCCESS, "Load Badge. BADGE_INDEX=" + badgeIdx, responseDto);
    }

    /**
     * 도전 과제 리스트 조회 API (완료)
     */
    @ResponseBody
    @GetMapping("/badges/all")
    public ResponseEntity<ResponseApiMessage> getBadges() {
        List<BadgeResponseDto> responseDtoList = badgeService.getBadgesByPurpose("badge");

        return sendResponseHttpByJson(SUCCESS, "Load Badge List.", responseDtoList);
    }

    /**
     * 티어 리스트 조회 API (완료)
     * @return
     */
    @ResponseBody
    @GetMapping("/tiers/all")
    public ResponseEntity<ResponseApiMessage> getTiers() {
        List<BadgeResponseDto> responseDtoList = badgeService.getBadgesByPurpose("tier");

        return sendResponseHttpByJson(SUCCESS, "Load Tier List.", responseDtoList);
    }

    /**
     * 프로필 사진 리스트 조회 API (완료)
     */
    @ResponseBody
    @GetMapping("/pfps/all")
    public ResponseEntity<ResponseApiMessage> getPfps() {
        List<BadgeResponseDto> responseDtoList = badgeService.getBadgesByPurpose("pfp");

        return sendResponseHttpByJson(SUCCESS, "Load Pfp List.", responseDtoList);
    }

}