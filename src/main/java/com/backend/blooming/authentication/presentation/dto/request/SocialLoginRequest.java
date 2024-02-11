package com.backend.blooming.authentication.presentation.dto.request;

public record SocialLoginRequest(String accessToken, String deviceToken) {
}
