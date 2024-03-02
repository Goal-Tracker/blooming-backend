package com.backend.blooming.notification.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class NotFoundGoalManagerException extends BloomingException {

    public NotFoundGoalManagerException() {
        super(ExceptionMessage.NOT_FOUND_MANAGER);
    }
}
