package com.backend.blooming.goal.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class ForbiddenGoalToPokeException extends BloomingException {

    private ForbiddenGoalToPokeException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class SenderNotInGoalTeam extends ForbiddenGoalToPokeException {

        public SenderNotInGoalTeam() {
            super(ExceptionMessage.SENDER_NOT_IN_GOAL_TEAM);
        }
    }

    public static class ReceiverNotInGoalTeam extends ForbiddenGoalToPokeException {

        public ReceiverNotInGoalTeam() {
            super(ExceptionMessage.RECEIVER_NOT_IN_GOAL_TEAM);
        }
    }
}
