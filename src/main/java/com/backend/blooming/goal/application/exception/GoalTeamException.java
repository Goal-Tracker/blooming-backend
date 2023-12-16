package com.backend.blooming.goal.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class GoalTeamException extends BloomingException {

    public GoalTeamException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class GoalTeamNotFoundException extends GoalTeamException{

        public GoalTeamNotFoundException(){
            super(ExceptionMessage.GOAL_NOT_FOUND);
        }
    }
}
