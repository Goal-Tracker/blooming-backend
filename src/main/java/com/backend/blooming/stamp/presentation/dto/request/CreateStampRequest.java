package com.backend.blooming.stamp.presentation.dto.request;

public record CreateStampRequest(
        long day,
        String message
) {
}
