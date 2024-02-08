package com.backend.blooming.authentication.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class AlreadyRegisterBlackListTokenException extends BloomingException {

    public AlreadyRegisterBlackListTokenException() {
        super(ExceptionMessage.ALREADY_REGISTER_BLACK_LIST_TOKEN);
    }
}
