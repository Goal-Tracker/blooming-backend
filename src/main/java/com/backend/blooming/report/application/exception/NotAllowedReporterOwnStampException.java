package com.backend.blooming.report.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class NotAllowedReporterOwnStampException extends BloomingException {

    public NotAllowedReporterOwnStampException() {
        super(ExceptionMessage.NOT_ALLOWED_REPORTER_OWN_STAMP);
    }
}
