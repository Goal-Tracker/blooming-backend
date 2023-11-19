package com.backend.blooming.authentication.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class UnauthorizedAccessTokenException extends BloomingException {

    public UnauthorizedAccessTokenException() {
        super(ExceptionMessage.UNAUTHORIZED_ACCESS_TOKEN);
    }
}
