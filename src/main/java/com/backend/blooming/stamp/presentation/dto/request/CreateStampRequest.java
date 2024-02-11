package com.backend.blooming.stamp.presentation.dto.request;

public record CreateStampRequest(
        Long goalId,
        int day,
        String message
) {
}
