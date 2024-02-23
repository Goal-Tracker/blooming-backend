package com.backend.blooming.report.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class GoalReportForbiddenException extends BloomingException {

    public GoalReportForbiddenException() {
        super(ExceptionMessage.GOAL_REPORT_FORBIDDEN);
    }
}
