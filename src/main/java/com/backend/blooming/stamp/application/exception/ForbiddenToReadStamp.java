package com.backend.blooming.stamp.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class ForbiddenToReadStamp extends BloomingException {

    public ForbiddenToReadStamp() {
        super(ExceptionMessage.READ_STAMP_FORBIDDEN);
    }
}
