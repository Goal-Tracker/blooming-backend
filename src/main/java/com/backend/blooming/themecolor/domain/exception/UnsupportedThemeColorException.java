package com.backend.blooming.themecolor.domain.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class UnsupportedThemeColorException extends BloomingException {

    public UnsupportedThemeColorException() {
        super(ExceptionMessage.UNSUPPORTED_THEME_COLOR);
    }
}
