package com.backend.blooming.report.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class StampReportForbiddenException extends BloomingException {

    public StampReportForbiddenException() {
        super(ExceptionMessage.STAMP_REPORT_FORBIDDEN);
    }
}
