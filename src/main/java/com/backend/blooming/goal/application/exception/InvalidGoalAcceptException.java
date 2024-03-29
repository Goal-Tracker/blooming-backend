package com.backend.blooming.goal.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class InvalidGoalAcceptException extends BloomingException {

    private InvalidGoalAcceptException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class InvalidInvalidUserToAcceptGoal extends InvalidGoalAcceptException {

        public InvalidInvalidUserToAcceptGoal() {
            super(ExceptionMessage.INVALID_GOAL_ACCEPT);
        }
    }

    public static class InvalidInvalidGoalAcceptByManager extends InvalidGoalAcceptException {

        public InvalidInvalidGoalAcceptByManager() {
            super(ExceptionMessage.INVALID_GOAL_ACCEPT_MANAGER);
        }
    }
}
