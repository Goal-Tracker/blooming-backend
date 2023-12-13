package com.backend.blooming.themecolor.presentation.response;

import com.backend.blooming.themecolor.application.dto.ThemeColorDto;

public record ThemeColorResponse(String name, String code) {

    public static ThemeColorResponse from(final ThemeColorDto themeColorDto) {
        return new ThemeColorResponse(themeColorDto.name(), themeColorDto.code());
    }
}
