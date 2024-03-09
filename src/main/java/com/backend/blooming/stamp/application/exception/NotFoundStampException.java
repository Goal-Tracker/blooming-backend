package com.backend.blooming.stamp.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class NotFoundStampException extends BloomingException {

    public NotFoundStampException() {
        super(ExceptionMessage.NOT_FOUND_STAMP);
    }
}
