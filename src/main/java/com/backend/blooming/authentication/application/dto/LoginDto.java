package com.backend.blooming.authentication.application.dto;

public record LoginDto(String oAuthAccessToken, String deviceToken) {

    public static LoginDto of(final String accessToken, final String deviceToken) {
        return new LoginDto(accessToken, deviceToken);
    }
}
