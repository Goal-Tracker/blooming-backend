package com.backend.blooming.user.application.dto;

import com.backend.blooming.user.presentation.request.UpdateUserRequest;

public record UpdateUserDto(String name, String color, String statusMessage) {

    public static UpdateUserDto from(final UpdateUserRequest updateUserRequest) {
        return new UpdateUserDto(
                updateUserRequest.name(),
                updateUserRequest.color(),
                updateUserRequest.statusMessage()
        );
    }
}
