package com.backend.blooming.user.application.dto;

import com.backend.blooming.user.presentation.dto.request.UpdateUserRequest;
import org.springframework.web.multipart.MultipartFile;

public record UpdateUserDto(
        String name,
        String color,
        String statusMessage,
        boolean changeToDefaultProfile,
        MultipartFile profileImage
) {

    public static UpdateUserDto of(final UpdateUserRequest updateUserRequest, final MultipartFile profileImage) {
        return new UpdateUserDto(
                updateUserRequest.name(),
                updateUserRequest.color(),
                updateUserRequest.statusMessage(),
                updateUserRequest.changeToDefaultProfile(),
                profileImage
        );
    }
}
