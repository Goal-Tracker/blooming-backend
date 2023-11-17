package com.backend.blooming.user.application.exception;

public class NotFoundUserException extends IllegalArgumentException {

    public NotFoundUserException(final String message) {
        super(message);
    }
}
