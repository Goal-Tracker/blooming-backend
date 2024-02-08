package com.backend.blooming.authentication.presentation.dto;

public record LogoutRequest(String refreshToken, String deviceToken) {
}
