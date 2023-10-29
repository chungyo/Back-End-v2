package com.mmos.mmos.config.exception;

import com.mmos.mmos.config.HttpResponseStatus;

public class NotAuthorizedAccessException extends BaseException {
    public NotAuthorizedAccessException(HttpResponseStatus message) {
        super(message);
    }
}
