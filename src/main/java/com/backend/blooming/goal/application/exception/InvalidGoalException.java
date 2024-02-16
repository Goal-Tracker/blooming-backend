package com.backend.blooming.goal.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class InvalidGoalException extends BloomingException {

    private InvalidGoalException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class InvalidInvalidGoalStartDate extends InvalidGoalException {

        public InvalidInvalidGoalStartDate() {
            super(ExceptionMessage.INVALID_GOAL_START_DATE);
        }
    }

    public static class InvalidInvalidGoalEndDate extends InvalidGoalException {

        public InvalidInvalidGoalEndDate() {
            super(ExceptionMessage.INVALID_GOAL_END_DATE);
        }
    }

    public static class InvalidInvalidGoalPeriod extends InvalidGoalException {

        public InvalidInvalidGoalPeriod() {
            super(ExceptionMessage.INVALID_GOAL_PERIOD);
        }
    }

    public static class InvalidInvalidGoalDays extends InvalidGoalException {

        public InvalidInvalidGoalDays() {
            super(ExceptionMessage.INVALID_GOAL_DAYS);
        }
    }

    public static class InvalidInvalidUsersSize extends InvalidGoalException {

        public InvalidInvalidUsersSize() {
            super(ExceptionMessage.INVALID_USERS_SIZE);
        }
    }

    public static class InvalidInvalidUserToParticipate extends InvalidGoalException {

        public InvalidInvalidUserToParticipate() {
            super(ExceptionMessage.INVALID_USER_TO_PARTICIPATE);
        }
    }

    public static class InvalidInvalidGoalName extends InvalidGoalException {

        public InvalidInvalidGoalName() {
            super(ExceptionMessage.INVALID_GOAL_NAME);
        }
    }

    public static class InvalidInvalidUpdateEndDate extends InvalidGoalException {

        public InvalidInvalidUpdateEndDate() {
            super(ExceptionMessage.INVALID_UPDATE_END_DATE);
        }
    }
}
