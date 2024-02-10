package com.backend.blooming.goal.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class UpdateGoalForbiddenException extends BloomingException {

    private UpdateGoalForbiddenException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class ForbiddenUserToUpdate extends UpdateGoalForbiddenException {

        public ForbiddenUserToUpdate() {
            super(ExceptionMessage.UPDATE_GOAL_FORBIDDEN);
        }
    }
}
