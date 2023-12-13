package com.backend.blooming.authentication.infrastructure.oauth;

import com.backend.blooming.authentication.infrastructure.exception.UnsupportedOAuthTypeException;

public enum OAuthType {

    KAKAO;

    public static OAuthType from(final String type) {
        try {
            return OAuthType.valueOf(type.toUpperCase());
        } catch (final IllegalArgumentException exception) {
            throw new UnsupportedOAuthTypeException();
        }
    }
}
