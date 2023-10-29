package com.mmos.mmos.config.exception;

import com.mmos.mmos.config.HttpResponseStatus;

public class OutOfRangeException extends BaseException {
    public OutOfRangeException(HttpResponseStatus message) {
        super(message);
    }
}