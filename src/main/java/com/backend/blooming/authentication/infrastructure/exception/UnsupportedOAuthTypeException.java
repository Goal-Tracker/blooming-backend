package com.backend.blooming.authentication.infrastructure.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class UnsupportedOAuthTypeException extends BloomingException {

    public UnsupportedOAuthTypeException() {
        super(ExceptionMessage.UNSUPPORTED_OAUTH_TYPE);
    }
}
