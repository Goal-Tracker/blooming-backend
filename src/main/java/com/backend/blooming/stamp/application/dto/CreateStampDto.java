package com.backend.blooming.stamp.application.dto;

import com.backend.blooming.stamp.presentation.dto.request.CreateStampRequest;
import org.springframework.web.multipart.MultipartFile;

public record CreateStampDto(
        Long goalId,
        Long userId,
        long day,
        String message,
        MultipartFile stampImage
) {
    
    public static CreateStampDto of(
            final CreateStampRequest request,
            final Long goalId,
            final Long userId,
            final MultipartFile stampImage
    ) {
        return new CreateStampDto(
                goalId,
                userId,
                request.day(),
                request.message(),
                stampImage
        );
    }
}
