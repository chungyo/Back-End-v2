package com.mmos.mmos.config;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseApiMessage {

    HttpResponseStatus httpResponseStatus;
    String message;
    Object responseData;

    @Builder
    public ResponseApiMessage(HttpResponseStatus httpResponseStatus, String message, Object responseData){
        this.httpResponseStatus = httpResponseStatus;
        this.message = message;
        this.responseData = responseData;
    }
}
