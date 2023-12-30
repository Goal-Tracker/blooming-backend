package com.backend.blooming.themecolor.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.themecolor.application.dto.ReadThemeColorDto;
import com.backend.blooming.themecolor.domain.ThemeColor;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ThemeColorServiceTest {

    @Autowired
    private ThemeColorService themeColorService;

    @Test
    void 테마_색상_목록을_조회한다() {
        // when
        final List<ReadThemeColorDto> actual = themeColorService.readAll();

        // then
        assertSoftly(softAssertions -> {
            final ThemeColor[] themeColors = ThemeColor.values();
            softAssertions.assertThat(actual).hasSize(themeColors.length);

            for (int i = 0; i < themeColors.length; i++) {
                softAssertions.assertThat(actual.get(i).name()).isEqualTo(themeColors[i].name());
                softAssertions.assertThat(actual.get(i).code()).isEqualTo(themeColors[i].getCode());
            }
        });
    }
}
