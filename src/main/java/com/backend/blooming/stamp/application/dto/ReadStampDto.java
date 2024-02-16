package com.backend.blooming.stamp.application.dto;

import com.backend.blooming.stamp.domain.Stamp;

public record ReadStampDto(
        Long goalId,
        int day,
        String message
) {

    public static ReadStampDto from(final Stamp stamp) {
        return new ReadStampDto(
                stamp.getGoal().getId(),
                stamp.getDay(),
                stamp.getMessage()
        );
    }
}
