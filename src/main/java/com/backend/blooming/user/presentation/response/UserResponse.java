package com.backend.blooming.user.presentation.response;

import com.backend.blooming.user.application.dto.UserDto;

public record UserResponse(
        Long id,
        String oAuthId,
        String oAuthType,
        String email,
        String name,
        String color,
        String statusMessage
) {

    public static UserResponse from(final UserDto userDto) {
        return new UserResponse(
                userDto.id(),
                userDto.oAuthId(),
                userDto.oAuthType(),
                userDto.email(),
                userDto.name(),
                userDto.color(),
                userDto.statusMessage()
        );
    }
}
