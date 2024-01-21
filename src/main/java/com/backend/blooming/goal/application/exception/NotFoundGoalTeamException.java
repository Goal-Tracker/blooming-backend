package com.backend.blooming.goal.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class NotFoundGoalTeamException extends BloomingException {

    public NotFoundGoalTeamException() {
        super(ExceptionMessage.GOAL_NOT_FOUND);
    }
}
