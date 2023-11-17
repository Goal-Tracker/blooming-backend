package com.backend.blooming.user.application.dto;

import com.backend.blooming.user.domain.User;

public record UserDto(
        Long id,
        String oAuthId,
        String oAuthType,
        String email,
        String name,
        String color,
        String statusMessage
) {

    public static UserDto from(final User user) {
        return new UserDto(
                user.getId(),
                user.getOAuthId(),
                user.getOAuthType().name(),
                user.getEmail(),
                user.getName(),
                user.getColor(),
                user.getStatusMessage()
        );
    }
}
