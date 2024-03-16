package com.backend.blooming.report.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class InvalidUserReportException extends BloomingException {

    private InvalidUserReportException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class AlreadyReportUserException extends InvalidUserReportException {

        public AlreadyReportUserException() {
            super(ExceptionMessage.ALREADY_REPORT_USER);
        }
    }

    public static class NotAllowedReportOwnUserException extends InvalidUserReportException {

        public NotAllowedReportOwnUserException() {
            super(ExceptionMessage.NOT_ALLOWED_REPORT_OWN_USER);
        }
    }
}
