package com.backend.blooming.user.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class DuplicateUserNameException extends BloomingException {

    public DuplicateUserNameException() {
        super(ExceptionMessage.DUPLICATE_USER_NAME);
    }
}
