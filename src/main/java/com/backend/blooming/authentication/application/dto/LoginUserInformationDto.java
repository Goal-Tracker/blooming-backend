package com.backend.blooming.authentication.application.dto;

import com.backend.blooming.user.domain.User;

public record LoginUserInformationDto(User user, boolean isSignUp) {
}
