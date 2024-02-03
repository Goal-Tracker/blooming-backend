package com.backend.blooming.goal.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class DeleteGoalForbiddenException extends BloomingException {

    public DeleteGoalForbiddenException() {
        super(ExceptionMessage.DELETE_GOAL_FORBIDDEN);
    }
}
