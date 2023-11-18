package com.backend.blooming.themecolor.domain.exception;

public class UnsupportedThemeColorException extends IllegalArgumentException {

    public UnsupportedThemeColorException(final String message) {
        super(message);
    }
}
