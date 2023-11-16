package com.backend.blooming.authentication.infrastructure.exception;

public class OAuthException extends IllegalArgumentException {

    public OAuthException(final String message) {
        super(message);
    }

    public static class InvalidAuthorizationTokenException extends OAuthException {

        public InvalidAuthorizationTokenException(final String message) {
            super(message);
        }
    }

    public static class KakaoServerException extends OAuthException {

        public KakaoServerException(final String message) {
            super(message);
        }
    }
}
