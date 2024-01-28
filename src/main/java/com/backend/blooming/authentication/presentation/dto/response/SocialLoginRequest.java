package com.backend.blooming.authentication.presentation.dto.response;

public record SocialLoginRequest(String accessToken, String deviceToken) {
}
