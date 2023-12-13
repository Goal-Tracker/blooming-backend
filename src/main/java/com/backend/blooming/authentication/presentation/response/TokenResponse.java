package com.backend.blooming.authentication.presentation.response;

import com.backend.blooming.authentication.application.dto.TokenDto;

public record TokenResponse(String accessToken, String refreshToken) {

    public static TokenResponse from(final TokenDto tokenDto) {
        return new TokenResponse(tokenDto.accessToken(), tokenDto.refreshToken());
    }
}
