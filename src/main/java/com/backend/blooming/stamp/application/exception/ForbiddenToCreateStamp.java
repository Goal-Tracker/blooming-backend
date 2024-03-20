package com.backend.blooming.stamp.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class ForbiddenToCreateStamp extends BloomingException {
    
    public ForbiddenToCreateStamp() {
        super(ExceptionMessage.CREATE_STAMP_FORBIDDEN);
    }
}
