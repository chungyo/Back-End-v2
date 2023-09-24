package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.userbadge.UserBadgeResponseDto;
import com.mmos.mmos.src.service.UserBadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@RestController
@RequestMapping("/api/v1/userbadges")
@RequiredArgsConstructor
public class UserBadgeController extends BaseController{
    private final UserBadgeService userbadgeService;

    @ResponseBody
    @PostMapping("/{userIdx}")
    // 티어, 도전과제, 프사 획득
    public ResponseEntity<ResponseApiMessage> saveUserBadge(@PathVariable Long userIdx) {
        List<UserBadgeResponseDto> responseDtoList = userbadgeService.saveUserBadge(userIdx);

        if(responseDtoList == null)
            return sendResponseHttpByJson(POST_BADGE_INVALID_REQUEST, "User can't get any badge.", null);
        return sendResponseHttpByJson(SUCCESS, "Saved UserBadge.", responseDtoList);
    }

    // 내 도전과제 보기
    @ResponseBody
    @GetMapping("/all/{userIdx}")
    public ResponseEntity<ResponseApiMessage> getUserBadges(@PathVariable Long userIdx) {
        List<UserBadgeResponseDto> responseDtoList = userbadgeService.getBadges(userIdx);

        if(responseDtoList == null)
            return sendResponseHttpByJson(GET_USERBADGE_EMPTY_LIST, "Empty UserBadge.", null);
        return sendResponseHttpByJson(SUCCESS, "Load UserBadge.", responseDtoList);
    }

    // 내 티어 보기
    @ResponseBody
    @GetMapping("/{userIdx}")
    public ResponseEntity<ResponseApiMessage> getTier(@PathVariable Long userIdx) {
        UserBadgeResponseDto responseDto = userbadgeService.getTier(userIdx);

        return sendResponseHttpByJson(SUCCESS, "Load Tier.", responseDto);
    }
}
