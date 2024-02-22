package com.backend.blooming.report.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class AlreadyReportUserException extends BloomingException {

    public AlreadyReportUserException() {
        super(ExceptionMessage.ALREADY_REPORT_USER);
    }
}
