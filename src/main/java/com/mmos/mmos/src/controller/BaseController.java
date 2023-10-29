package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.config.ResponseApiMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.nio.charset.StandardCharsets;

@Controller
class BaseController {

    /**
     * JSON 형식으로 HTTP 응답을 생성하여 반환하는 메소드
     *
     * @param httpResponseStatus   HTTP 상태 코드
     * @param message      응답 메시지
     * @param responseData 응답 데이터
     * @return ResponseEntity 객체, JSON 응답, HTTP 상태 코드
     */

    public ResponseEntity<ResponseApiMessage> sendResponseHttpByJson(HttpResponseStatus httpResponseStatus, String message, Object responseData) {
        ResponseApiMessage responseApiMessage = ResponseApiMessage.builder()
                .httpResponseStatus(httpResponseStatus)
                .message(message)
                .responseData(responseData)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(responseApiMessage, headers, 200);
    }
}
