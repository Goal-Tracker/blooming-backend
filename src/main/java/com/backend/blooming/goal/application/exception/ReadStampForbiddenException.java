package com.backend.blooming.goal.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class ReadStampForbiddenException extends BloomingException {
    public ReadStampForbiddenException() {
        super(ExceptionMessage.DELETE_FRIEND_FORBIDDEN);
    }
}
