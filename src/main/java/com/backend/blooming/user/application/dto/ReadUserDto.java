package com.backend.blooming.user.application.dto;

import com.backend.blooming.user.domain.User;

public record ReadUserDto(
        Long id,
        String oAuthId,
        String oAuthType,
        String email,
        String name,
        String profileImageUrl,
        String color,
        String statusMessage,
        boolean hasNewAlarm
) {

    public static ReadUserDto from(final User user) {
        return new ReadUserDto(
                user.getId(),
                user.getOAuthId(),
                user.getOAuthType().name(),
                user.getEmail(),
                user.getName(),
                user.getProfileImageUrl(),
                user.getColorCode(),
                user.getStatusMessage(),
                user.isNewAlarm()
        );
    }
}
