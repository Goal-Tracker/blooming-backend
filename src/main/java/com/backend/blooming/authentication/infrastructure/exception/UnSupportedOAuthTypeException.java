package com.backend.blooming.authentication.infrastructure.exception;

public class UnSupportedOAuthTypeException extends IllegalArgumentException {

    public UnSupportedOAuthTypeException(final String message) {
        super(message);
    }
}
