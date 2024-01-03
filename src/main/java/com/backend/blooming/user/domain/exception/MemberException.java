package com.backend.blooming.user.domain.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class MemberException extends BloomingException {

    private MemberException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class NullOrEmptyEmailException extends MemberException {

        public NullOrEmptyEmailException() {
            super(ExceptionMessage.NULL_OR_EMPTY_EMAIL);
        }
    }

    public static class LongerThanMaximumEmailLengthException extends MemberException {

        public LongerThanMaximumEmailLengthException() {
            super(ExceptionMessage.LONGER_THAN_MAXIMUM_EMAIL);
        }
    }

    public static class InvalidEmailFormatException extends MemberException {

        public InvalidEmailFormatException() {
            super(ExceptionMessage.INVALID_EMAIL_FORMAT);
        }
    }
}
