package com.backend.blooming.stamp.domain.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class InvalidStampException extends BloomingException {
    
    public InvalidStampException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }
    
    public static class InvalidStampDay extends InvalidStampException {
        
        public InvalidStampDay() {
            super(ExceptionMessage.INVALID_STAMP_DAY);
        }
    }
    
    public static class InvalidStampDayFuture extends InvalidStampException {
        
        public InvalidStampDayFuture() {
            super(ExceptionMessage.INVALID_STAMP_TO_CREATE);
        }
    }
    
    public static class InvalidStampToCreate extends InvalidStampException {
        
        public InvalidStampToCreate() {
            super(ExceptionMessage.INVALID_STAMP_TO_CREATE);
        }
    }
    
    public static class InvalidStampMessage extends InvalidStampException {
        
        public InvalidStampMessage() {
            super(ExceptionMessage.INVALID_STAMP_MESSAGE);
        }
    }
}
