package com.backend.blooming.goal.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class GoalException extends BloomingException {

    private GoalException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class GoalNotFoundException extends GoalException {

        public GoalNotFoundException() {
            super(ExceptionMessage.GOAL_NOT_FOUND);
        }
    }

    public static class InvalidGoalStartDay extends GoalException {

        public InvalidGoalStartDay() {
            super(ExceptionMessage.INVALID_GOAL_START_DAY);
        }
    }

    public static class InvalidGoalEndDay extends GoalException {

        public InvalidGoalEndDay() {
            super(ExceptionMessage.INVALID_GOAL_END_DAY);
        }
    }

    public static class InvalidGoalPeriod extends GoalException {

        public InvalidGoalPeriod() {
            super(ExceptionMessage.INVALID_GOAL_PERIOD);
        }
    }

    public static class InvalidGoalDays extends GoalException {

        public InvalidGoalDays() {
            super(ExceptionMessage.INVALID_GOAL_DAYS);
        }
    }
}
