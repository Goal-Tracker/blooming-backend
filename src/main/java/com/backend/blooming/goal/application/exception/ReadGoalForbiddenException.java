package com.backend.blooming.goal.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class ReadGoalForbiddenException extends BloomingException {

    public ReadGoalForbiddenException() {
        super(ExceptionMessage.READ_GOAL_FORBIDDEN);
    }
}
