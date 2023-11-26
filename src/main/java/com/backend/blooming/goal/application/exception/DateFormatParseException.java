package com.backend.blooming.goal.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class DateFormatParseException extends BloomingException {

    public DateFormatParseException() {
        super(ExceptionMessage.DATE_FORMAT_PARSE_FAILED);
    }
}
