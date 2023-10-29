package com.mmos.mmos.config.exception;

import com.mmos.mmos.config.HttpResponseStatus;

public class EmptyInputException extends BaseException {
    public EmptyInputException(HttpResponseStatus message) {
        super(message);
    }
}