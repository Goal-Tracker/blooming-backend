package com.backend.blooming.themecolor.application.dto;

import com.backend.blooming.themecolor.domain.ThemeColor;

public record ReadThemeColorDto(String name, String code) {

    public static ReadThemeColorDto from(final ThemeColor themeColor) {
        return new ReadThemeColorDto(themeColor.name(), themeColor.getCode());
    }
}
