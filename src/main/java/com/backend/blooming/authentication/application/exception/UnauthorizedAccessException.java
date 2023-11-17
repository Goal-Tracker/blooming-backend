package com.backend.blooming.authentication.application.exception;

public class UnauthorizedAccessException extends IllegalArgumentException {

    public UnauthorizedAccessException(final String message) {
        super(message);
    }
}
