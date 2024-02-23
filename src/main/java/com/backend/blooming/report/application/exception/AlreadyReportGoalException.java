package com.backend.blooming.report.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class AlreadyReportGoalException extends BloomingException {

    public AlreadyReportGoalException() {
        super(ExceptionMessage.ALREADY_REPORT_GOAL);
    }
}
