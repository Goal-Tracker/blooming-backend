package com.backend.blooming.goal.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class InvalidGoalException extends BloomingException {

    private InvalidGoalException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class InvalidInvalidGoalStartDay extends InvalidGoalException {

        public InvalidInvalidGoalStartDay() {
            super(ExceptionMessage.INVALID_GOAL_START_DAY);
        }
    }

    public static class InvalidInvalidGoalEndDay extends InvalidGoalException {

        public InvalidInvalidGoalEndDay() {
            super(ExceptionMessage.INVALID_GOAL_END_DAY);
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
            super(ExceptionMessage.INVALID_GOAL_DAYS);
        }
    }

    public static class InvalidInvalidUserToParticipate extends InvalidGoalException {

        public InvalidInvalidUserToParticipate() {
            super(ExceptionMessage.INVALID_USER_TO_PARTICIPATE);
        }
    }
}
