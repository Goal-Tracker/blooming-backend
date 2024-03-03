package com.backend.blooming.report.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class ReportForbiddenException extends BloomingException {

    public ReportForbiddenException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class GoalReportForbiddenException extends ReportForbiddenException {

        public GoalReportForbiddenException() {
            super(ExceptionMessage.GOAL_REPORT_FORBIDDEN);
        }
    }

    public static class StampReportForbiddenException extends ReportForbiddenException {

        public StampReportForbiddenException() {
            super(ExceptionMessage.STAMP_REPORT_FORBIDDEN);
        }
    }
}
