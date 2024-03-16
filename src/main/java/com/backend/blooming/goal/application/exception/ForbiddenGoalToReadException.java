package com.backend.blooming.goal.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class ForbiddenGoalToReadException extends BloomingException {

    public ForbiddenGoalToReadException() {
        super(ExceptionMessage.FORBIDDEN_USER_TO_READ_GOAL);
    }
}
