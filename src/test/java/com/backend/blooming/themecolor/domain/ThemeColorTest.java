package com.backend.blooming.themecolor.domain;

import com.backend.blooming.themecolor.domain.exception.UnsupportedThemeColorException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ThemeColorTest {

    @ParameterizedTest(name = "테마 색상 문자열: {0}")
    @ValueSource(strings = {"BEIGE", "Beige", "beige"})
    void 테마_색상을_문자열로_조회할시_테마_색상을_반환한다(final String themeColorName) {
        // when
        final ThemeColor actual = ThemeColor.from(themeColorName);

        // then
        assertThat(actual).isEqualTo(ThemeColor.BEIGE);
    }

    @Test
    void 존재하지_않는_테마_색상의_문자열로_조회할시_예외를_반환한다() {
        // given
        final String invalidThemeColor = "invalidThemeColor";

        // when & then
        assertThatThrownBy(() -> ThemeColor.from(invalidThemeColor))
                .isInstanceOf(UnsupportedThemeColorException.class);
    }
}
