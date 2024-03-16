package com.backend.blooming.user.presentation.dto.response;

import com.backend.blooming.user.application.dto.ReadUserDto;

public record ReadUpdateUserResponse(
        Long id,
        String oAuthId,
        String oAuthType,
        String email,
        String name,
        String profileImageUrl,
        String color,
        String statusMessage
) {

    public static ReadUpdateUserResponse from(final ReadUserDto readUserDto) {
        return new ReadUpdateUserResponse(
                readUserDto.id(),
                readUserDto.oAuthId(),
                readUserDto.oAuthType(),
                readUserDto.email(),
                readUserDto.name(),
                readUserDto.profileImageUrl(),
                readUserDto.color(),
                readUserDto.statusMessage()
        );
    }
}
