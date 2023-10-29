package com.mmos.mmos.config.exception;

import com.mmos.mmos.config.HttpResponseStatus;

public class EmptyEntityException extends BaseException {
    public EmptyEntityException(HttpResponseStatus message) {
        super(message);
    }
}

