package com.backend.blooming.themecolor.presentation.response;

import com.backend.blooming.themecolor.application.dto.ThemeColorDto;

import java.util.List;

public record ThemeColorsResponse(List<ThemeColorResponse> themeColors) {
    public static ThemeColorsResponse from(final List<ThemeColorDto> themeColorDtos) {
        final List<ThemeColorResponse> themeColors = themeColorDtos.stream()
                                                                   .map(ThemeColorResponse::from)
                                                                   .toList();

        return new ThemeColorsResponse(themeColors);
    }
}
