package com.backend.blooming.themecolor.presentation;

import com.backend.blooming.themecolor.application.dto.ReadThemeColorsDto;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class ThemeColorControllerTestFixture {

    protected ReadThemeColorsDto 테마_색상_목록_dto = new ReadThemeColorsDto(
            List.of(
                    new ReadThemeColorsDto.ReadThemeColorDto("BEIGE", "#f5f1f0"),
                    new ReadThemeColorsDto.ReadThemeColorDto("BABY_PINK", "#f8c8c4")
            )
    );
}
