package com.backend.blooming.stamp.application.dto;

import com.backend.blooming.stamp.presentation.dto.request.CreateStampRequest;

public record CreateStampDto(
        Long goalId,
        Long userId,
        int day,
        String message
) {
    
    public static CreateStampDto of(final CreateStampRequest request, final Long userId) {
        return new CreateStampDto(
                request.goalId(),
                userId,
                request.day(),
                request.message()
        );
    }
}