package com.backend.blooming.stamp.presentation.dto.response;

import com.backend.blooming.stamp.application.dto.ReadStampDto;

public record ReadStampResponse(
        Long goalId,
        int day,
        String message
) {

    public static ReadStampResponse from(final ReadStampDto stamp) {
        return new ReadStampResponse(
                stamp.goalId(),
                stamp.day(),
                stamp.message()
        );
    }
}
