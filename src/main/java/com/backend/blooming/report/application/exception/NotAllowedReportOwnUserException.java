package com.backend.blooming.report.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class NotAllowedReportOwnUserException extends BloomingException {

    public NotAllowedReportOwnUserException() {
        super(ExceptionMessage.NOT_ALLOWED_REPORT_OWN_USER);
    }
}
