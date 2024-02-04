package com.backend.blooming.authentication.application.dto;

public record LogoutDto(String refreshToken, String deviceToken) {
}
