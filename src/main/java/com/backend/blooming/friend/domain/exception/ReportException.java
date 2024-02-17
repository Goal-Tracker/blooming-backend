package com.backend.blooming.friend.domain.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class ReportException extends BloomingException {

    private ReportException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class NullOrEmptyContentException extends ReportException {

        public NullOrEmptyContentException() {
            super(ExceptionMessage.NULL_OR_EMPTY_CONTENT);
        }
    }
}
