package com.backend.blooming.authentication.infrastructure.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class OAuthException extends BloomingException {

    private OAuthException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class InvalidAuthorizationTokenException extends OAuthException {

        public InvalidAuthorizationTokenException() {
            super(ExceptionMessage.INVALID_AUTHORIZATION_TOKEN);
        }
    }

    public static class KakaoServerUnavailableException extends OAuthException {

        public KakaoServerUnavailableException() {
            super(ExceptionMessage.KAKAO_SERVER_UNAVAILABLE);
        }
    }
}
