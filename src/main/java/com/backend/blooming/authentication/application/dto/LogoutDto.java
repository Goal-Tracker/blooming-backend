package com.backend.blooming.authentication.application.dto;

import com.backend.blooming.authentication.presentation.dto.request.LogoutRequest;

public record LogoutDto(String refreshToken, String deviceToken) {

    public static LogoutDto from(final LogoutRequest logoutRequest) {
        return new LogoutDto(logoutRequest.refreshToken(), logoutRequest.deviceToken());
    }
}
