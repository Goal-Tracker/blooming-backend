package com.backend.blooming.themecolor.presentation;

import com.backend.blooming.themecolor.application.dto.ReadThemeColorDto;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class ThemeColorControllerTestFixture {

    protected List<ReadThemeColorDto> 테마_색상_목록_dto = List.of(
            new ReadThemeColorDto("BEIGE", "#f5f1f0"),
            new ReadThemeColorDto("BABY_PINK", "#f8c8c4")
    );
}
