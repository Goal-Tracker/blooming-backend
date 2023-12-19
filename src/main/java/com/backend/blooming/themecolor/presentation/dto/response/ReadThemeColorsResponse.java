package com.backend.blooming.themecolor.presentation.dto.response;

import com.backend.blooming.themecolor.application.dto.ReadThemeColorDto;

import java.util.List;

public record ReadThemeColorsResponse(List<ReadThemeColorResponse> themeColors) {
    public static ReadThemeColorsResponse from(final List<ReadThemeColorDto> readThemeColorDtos) {
        final List<ReadThemeColorResponse> themeColors = readThemeColorDtos.stream()
                                                                           .map(ReadThemeColorResponse::from)
                                                                           .toList();

        return new ReadThemeColorsResponse(themeColors);
    }

    public record ReadThemeColorResponse(String name, String code) {

        public static ReadThemeColorResponse from(final ReadThemeColorDto readThemeColorDto) {
            return new ReadThemeColorResponse(readThemeColorDto.name(), readThemeColorDto.code());
        }
    }
}
