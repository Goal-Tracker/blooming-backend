package com.backend.blooming.report.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class InvalidGoalReportException extends BloomingException {

    private InvalidGoalReportException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class AlreadyReportGoalException extends InvalidGoalReportException {

        public AlreadyReportGoalException() {
            super(ExceptionMessage.ALREADY_REPORT_GOAL);
        }
    }

    public static class NotAllowedReportOwnGoalException extends InvalidGoalReportException {

        public NotAllowedReportOwnGoalException() {
            super(ExceptionMessage.NOT_ALLOWED_REPORT_OWN_STAMP);
        }
    }
}
