package com.backend.blooming.authentication.infrastructure.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class InvalidTokenException extends BloomingException {

    private InvalidTokenException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public InvalidTokenException() {
        super(ExceptionMessage.INVALID_TOKEN);
    }

    public static class WrongTokenTypeException extends InvalidTokenException {

        public WrongTokenTypeException() {
            super(ExceptionMessage.WRONG_TOKEN_TYPE);
        }
    }

    public static class ExpiredTokenException extends InvalidTokenException {

        public ExpiredTokenException() {
            super(ExceptionMessage.EXPIRED_TOKEN);
        }
    }
}
