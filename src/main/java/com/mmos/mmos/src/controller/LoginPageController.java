package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.src.domain.dto.request.LoginRequestDto;
import com.mmos.mmos.src.service.AuthService;
import com.mmos.mmos.src.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.mmos.mmos.config.HttpResponseStatus.LOGIN_FAIL;
import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
public class LoginPageController extends BaseController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseApiMessage> login(@RequestBody LoginRequestDto requestDto) {
        try {
            if(requestDto.getId().isEmpty()
                    || requestDto.getPwd().isEmpty())
                throw new BaseException(LOGIN_FAIL);

            return sendResponseHttpByJson(SUCCESS, "로그인 성공",
                    authService.authenticate(requestDto));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }


}
