package com.backend.blooming.report.domain.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class NullOrEmptyContentException extends BloomingException {

    public NullOrEmptyContentException() {
        super(ExceptionMessage.NULL_OR_EMPTY_CONTENT);
    }
}
