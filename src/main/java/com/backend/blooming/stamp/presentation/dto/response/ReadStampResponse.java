package com.backend.blooming.stamp.presentation.dto.response;

import com.backend.blooming.stamp.application.dto.ReadStampDto;

public record ReadStampResponse(
        Long id,
        String userName,
        String userColor,
        long day,
        String message
) {

    public static ReadStampResponse from(final ReadStampDto stamp) {
        return new ReadStampResponse(
                stamp.id(),
                stamp.userName(),
                stamp.userColor().getCode(),
                stamp.day(),
                stamp.message()
        );
    }
}
