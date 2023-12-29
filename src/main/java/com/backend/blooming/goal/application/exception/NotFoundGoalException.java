package com.backend.blooming.goal.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class NotFoundGoalException extends BloomingException {

    public NotFoundGoalException() {
        super(ExceptionMessage.GOAL_NOT_FOUND);
    }
}
