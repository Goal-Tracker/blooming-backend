package com.backend.blooming.stamp.application.dto;

import com.backend.blooming.stamp.domain.Stamp;
import com.backend.blooming.themecolor.domain.ThemeColor;

public record ReadStampDto(
        Long id,
        String userName,
        ThemeColor userColor,
        long day,
        String message,
        String stampImageUrl
) {

    public static ReadStampDto from(final Stamp stamp) {
        return new ReadStampDto(
                stamp.getGoal().getId(),
                stamp.getUser().getName(),
                stamp.getUser().getColor(),
                stamp.getDay(),
                stamp.getMessage(),
                stamp.getStampImageUrl()
        );
    }
}
