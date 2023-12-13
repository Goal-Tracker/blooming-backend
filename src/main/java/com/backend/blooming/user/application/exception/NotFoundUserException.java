package com.backend.blooming.user.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class NotFoundUserException extends BloomingException {

    public NotFoundUserException() {
        super(ExceptionMessage.NOT_FOUND_USER);
    }
}
