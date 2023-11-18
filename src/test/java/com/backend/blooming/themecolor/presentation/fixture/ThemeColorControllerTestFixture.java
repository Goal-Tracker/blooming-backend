package com.backend.blooming.themecolor.presentation.fixture;

import com.backend.blooming.themecolor.application.dto.ThemeColorDto;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class ThemeColorControllerTestFixture {

    protected List<ThemeColorDto> 테마_색상_목록_dto = List.of(
            new ThemeColorDto("BEIGE", "#f5f1f0"),
            new ThemeColorDto("BABY_PINK", "#f8c8c4")
    );
}
