package com.backend.blooming.authentication.infrastructure.oauth;

import com.backend.blooming.authentication.infrastructure.exception.UnSupportedOAuthTypeException;

public enum OAuthType {

    KAKAO;

    public static OAuthType from(final String type) {
        try {
            return OAuthType.valueOf(type.toUpperCase());
        } catch (final IllegalArgumentException exception) {
            throw new UnSupportedOAuthTypeException("지원하지 않는 소셜 로그인 방식입니다.");
        }
    }
}
