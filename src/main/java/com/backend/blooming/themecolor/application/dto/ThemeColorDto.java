package com.backend.blooming.themecolor.application.dto;

import com.backend.blooming.themecolor.domain.ThemeColor;

public record ThemeColorDto(String name, String code) {

    public static ThemeColorDto from(final ThemeColor themeColor) {
        return new ThemeColorDto(themeColor.name(), themeColor.getCode());
    }
}
