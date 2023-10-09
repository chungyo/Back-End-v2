package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AccountController extends BaseController {

    private final MailService emailService;

    @PostMapping("login/mailConfirm")
    @ResponseBody
    public ResponseEntity<ResponseApiMessage> mailConfirm(@RequestParam String email) throws Exception {
        String code = emailService.sendSimpleMessage(email);
        log.info("인증코드 : " + code);
        return sendResponseHttpByJson(SUCCESS, "인증 완료", code);
    }
}