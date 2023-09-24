package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.userbadge.UserBadgeResponseDto;
import com.mmos.mmos.src.domain.entity.UserBadge;
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
    // 뱃지 획득
    public ResponseEntity<ResponseApiMessage> saveUserBadge(@PathVariable Long userIdx) {
        List<UserBadgeResponseDto> responseDtoList = userbadgeService.saveUserBadge(userIdx);

        if(responseDtoList == null)
            return sendResponseHttpByJson(POST_BADGE_INVALID_REQUEST, "User can't get any badge.", null);
        return sendResponseHttpByJson(SUCCESS, "Saved UserBadge.", responseDtoList);
    }
}
