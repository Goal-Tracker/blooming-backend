package com.backend.blooming.authentication.application.dto;

public record LoginInformationDto(TokenDto token, boolean isSignUp) {
}
