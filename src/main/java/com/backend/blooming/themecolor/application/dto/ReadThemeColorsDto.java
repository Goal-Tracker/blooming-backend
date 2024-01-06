package com.backend.blooming.themecolor.application.dto;

import com.backend.blooming.themecolor.domain.ThemeColor;

import java.util.List;

public record ReadThemeColorsDto(List<ReadThemeColorDto> colors) {

    public static ReadThemeColorsDto from(final List<ThemeColor> values) {
        final List<ReadThemeColorDto> colorDtos = values.stream()
                                                        .map(ReadThemeColorDto::from)
                                                        .toList();

        return new ReadThemeColorsDto(colorDtos);
    }

    public record ReadThemeColorDto(String name, String code) {

        public static ReadThemeColorDto from(final ThemeColor themeColor) {
            return new ReadThemeColorDto(themeColor.name(), themeColor.getCode());
        }
    }
}
