package com.backend.blooming.stamp.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class ReadStampForbiddenException extends BloomingException {

    public ReadStampForbiddenException() {
        super(ExceptionMessage.READ_STAMP_FORBIDDEN);
    }
}
