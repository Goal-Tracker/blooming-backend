package com.backend.blooming.authentication.infrastructure.exception;

public class OAuthException extends IllegalArgumentException {

    public OAuthException(final String message) {
        super(message);
    }
}
