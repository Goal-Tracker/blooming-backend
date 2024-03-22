package com.backend.blooming.stamp.presentation.dto.request;

public record CreateStampRequest(
        int day,
        String message
) {
}
