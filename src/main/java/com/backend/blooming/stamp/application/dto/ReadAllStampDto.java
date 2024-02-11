package com.backend.blooming.stamp.application.dto;

import com.backend.blooming.stamp.domain.Stamp;
import com.backend.blooming.themecolor.domain.ThemeColor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ReadAllStampDto(Map<Integer, List<StampDto>> stamps) {

    public static ReadAllStampDto from(final List<Stamp> stamps) {
        final List<StampDto> stampInfos= stamps.stream().map(StampDto::from).toList();

        return new ReadAllStampDto(
                stampInfos.stream().collect(Collectors.groupingBy(StampDto::day))
        );
    }

    public record StampDto(
            Long userId,
            ThemeColor userColor,
            int day
    ) {

        public static StampDto from(final Stamp stamp) {
            return new StampDto(
                    stamp.getUser().getId(),
                    stamp.getUser().getColor(),
                    stamp.getDay()
            );
        }
    }
}
