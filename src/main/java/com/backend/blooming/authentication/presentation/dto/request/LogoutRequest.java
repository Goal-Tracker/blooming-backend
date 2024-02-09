package com.backend.blooming.authentication.presentation.dto.request;

public record LogoutRequest(String refreshToken, String deviceToken) {
}
