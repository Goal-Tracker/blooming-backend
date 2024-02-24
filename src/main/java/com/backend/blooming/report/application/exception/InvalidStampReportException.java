package com.backend.blooming.report.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class InvalidStampReportException extends BloomingException {

    public InvalidStampReportException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class AlreadyReportStampException extends InvalidStampReportException {

        public AlreadyReportStampException() {
            super(ExceptionMessage.ALREADY_REPORT_STAMP);
        }
    }

    public static class NotAllowedReportOwnStampException extends InvalidStampReportException {

        public NotAllowedReportOwnStampException() {
            super(ExceptionMessage.NOT_ALLOWED_REPORT_OWN_STAMP);
        }
    }
}
