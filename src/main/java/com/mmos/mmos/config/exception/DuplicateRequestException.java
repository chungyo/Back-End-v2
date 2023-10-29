package com.mmos.mmos.config.exception;

import com.mmos.mmos.config.HttpResponseStatus;

public class DuplicateRequestException extends BaseException {
    public DuplicateRequestException(HttpResponseStatus message) {
        super(message);
    }
}

