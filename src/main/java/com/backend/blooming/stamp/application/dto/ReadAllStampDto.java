package com.backend.blooming.stamp.application.dto;

import com.backend.blooming.stamp.domain.Stamp;
import com.backend.blooming.themecolor.domain.ThemeColor;

import java.util.List;

public record ReadAllStampDto(List<StampDto> stamps) {

    public static ReadAllStampDto from(final List<Stamp> stamps) {
        return new ReadAllStampDto(stamps.stream().map(StampDto::from).toList());
    }

    public record StampDto(
            Long userId,
            String name,
            ThemeColor color,
            String message,
            int day
    ) {

        public static StampDto from(final Stamp stamp) {
            return new StampDto(
                    stamp.getUser().getId(),
                    stamp.getUser().getName(),
                    stamp.getUser().getColor(),
                    stamp.getMessage().getMessage(),
                    stamp.getDay().getDay()
            );
        }
    }
}
