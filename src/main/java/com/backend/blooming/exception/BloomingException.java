package com.backend.blooming.exception;

import lombok.Getter;

@Getter
public class BloomingException extends RuntimeException {

    private final String message;

    public BloomingException(final ExceptionMessage exceptionMessage) {
        this.message = exceptionMessage.getMessage();
    }
}
