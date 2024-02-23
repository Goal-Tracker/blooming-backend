package com.backend.blooming.report.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class AlreadyReportStampException extends BloomingException {

    public AlreadyReportStampException() {
        super(ExceptionMessage.ALREADY_REPORT_STAMP);
    }
}
