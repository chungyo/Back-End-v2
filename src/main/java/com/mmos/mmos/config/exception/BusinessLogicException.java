package com.mmos.mmos.config.exception;

import com.mmos.mmos.config.HttpResponseStatus;

public class BusinessLogicException extends BaseException {
    public BusinessLogicException(HttpResponseStatus message) {
        super(message);
    }
}
