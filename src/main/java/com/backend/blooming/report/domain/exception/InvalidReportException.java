package com.backend.blooming.report.domain.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class InvalidReportException extends BloomingException {

    private InvalidReportException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class NullOrEmptyContentException extends InvalidReportException {

        public NullOrEmptyContentException() {
            super(ExceptionMessage.NULL_OR_EMPTY_CONTENT);
        }
    }

    public static class SelfReportingNotAllowedException extends BloomingException {

        public SelfReportingNotAllowedException() {
            super(ExceptionMessage.SELF_REPORTING_NOT_ALLOWED);
        }
    }
}
