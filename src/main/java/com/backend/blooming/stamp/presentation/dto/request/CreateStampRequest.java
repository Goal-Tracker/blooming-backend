package com.backend.blooming.stamp.presentation.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record CreateStampRequest(
        int day,
        String message,

        MultipartFile stampImage
) {
}
