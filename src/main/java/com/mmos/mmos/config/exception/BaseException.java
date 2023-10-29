package com.mmos.mmos.config.exception;

import com.mmos.mmos.config.HttpResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends Exception {
    private HttpResponseStatus status;
}
