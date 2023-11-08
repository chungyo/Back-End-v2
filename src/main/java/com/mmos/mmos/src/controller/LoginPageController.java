package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.security.JwtTokenUtil;
import com.mmos.mmos.security.Secret;
import com.mmos.mmos.src.domain.dto.request.LoginRequestDto;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mmos.mmos.config.HttpResponseStatus.*;
import static com.mmos.mmos.security.Secret.*;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginPageController extends BaseController {

    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<ResponseApiMessage> login(@RequestBody LoginRequestDto requestDto) {
        try {
            if(requestDto.getId().isEmpty()
                    || requestDto.getPwd().isEmpty())
                throw new BaseException(LOGIN_FAIL);

            User user = userService.login(requestDto);
            System.out.println(user.getUserName());
            long expireTimeMs = 1000 * 60 * 60;

            return sendResponseHttpByJson(SUCCESS, "로그인 성공",
                    JwtTokenUtil.createToken(user.getUserId(), JWT_SECRET_KEY, expireTimeMs));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }


}
