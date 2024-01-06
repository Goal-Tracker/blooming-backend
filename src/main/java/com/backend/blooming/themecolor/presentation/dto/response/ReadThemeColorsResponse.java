package com.backend.blooming.themecolor.presentation.dto.response;

import com.backend.blooming.themecolor.application.dto.ReadThemeColorsDto;

import java.util.List;

public record ReadThemeColorsResponse(List<ReadThemeColorResponse> themeColors) {
    public static ReadThemeColorsResponse from(final ReadThemeColorsDto readThemeColorDtos) {
        final List<ReadThemeColorResponse> themeColors = readThemeColorDtos.colors()
                                                                           .stream()
                                                                           .map(ReadThemeColorResponse::from)
                                                                           .toList();

        return new ReadThemeColorsResponse(themeColors);
    }

    public record ReadThemeColorResponse(String name, String code) {

        public static ReadThemeColorResponse from(final ReadThemeColorsDto.ReadThemeColorDto readThemeColorDto) {
            return new ReadThemeColorResponse(readThemeColorDto.name(), readThemeColorDto.code());
        }
    }
}
