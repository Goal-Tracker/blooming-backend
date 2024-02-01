package com.backend.blooming.goal.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class UpdateGoalForbiddenException extends BloomingException {


    public UpdateGoalForbiddenException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class ForbiddenUserToUpdate extends UpdateGoalForbiddenException {

        public ForbiddenUserToUpdate() {
            super(ExceptionMessage.UPDATE_GOAL_FORBIDDEN);
        }
    }

    public static class ForbiddenEndDateToUpdate extends UpdateGoalForbiddenException {

        public ForbiddenEndDateToUpdate() {
            super(ExceptionMessage.UPDATE_END_DATE_FORBIDDEN);
        }
    }

    public static class ForbiddenTeamsToUpdate extends UpdateGoalForbiddenException {

        public ForbiddenTeamsToUpdate() {
            super(ExceptionMessage.UPDATE_TEAMS_FORBIDDEN);
        }
    }
}
