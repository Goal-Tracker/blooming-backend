package com.backend.blooming.user.presentation.dto.response;

import com.backend.blooming.user.application.dto.ReadUserDto;

public record ReadUserResponse(
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

    public static ReadUserResponse from(final ReadUserDto readUserDto) {
        return new ReadUserResponse(
                readUserDto.id(),
                readUserDto.oAuthId(),
                readUserDto.oAuthType(),
                readUserDto.email(),
                readUserDto.name(),
                readUserDto.profileImageUrl(),
                readUserDto.color(),
                readUserDto.statusMessage(),
                readUserDto.hasNewAlarm()
        );
    }
}
