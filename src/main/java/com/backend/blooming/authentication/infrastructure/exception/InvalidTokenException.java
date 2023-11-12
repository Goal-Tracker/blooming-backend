package com.backend.blooming.authentication.infrastructure.exception;

public class InvalidTokenException extends IllegalArgumentException {

    public InvalidTokenException(final String message) {
        super(message);
    }
}
