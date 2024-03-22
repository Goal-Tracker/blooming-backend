package com.backend.blooming.stamp.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class ForbiddenStampToReadException extends BloomingException {

    public ForbiddenStampToReadException() {
        super(ExceptionMessage.READ_STAMP_FORBIDDEN);
    }
}
