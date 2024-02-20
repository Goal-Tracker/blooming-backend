package com.backend.blooming.stamp.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class CreateStampForbiddenException extends BloomingException {
    
    public CreateStampForbiddenException() {
        super(ExceptionMessage.CREATE_STAMP_FORBIDDEN);
    }
}
