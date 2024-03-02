package com.backend.blooming.user.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProfileImageUrlTest {

    @Test
    void 프로필_이미지_url을_생성한다() {
        // given
        final String profileImageUrl = "https://profile.image.url";

        // when
        final ProfileImageUrl actual = new ProfileImageUrl(profileImageUrl);

        // then
        assertThat(actual.getValue()).isEqualTo(profileImageUrl);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 프로필_이미지_url_생성시_null_혹은_빈값이라면_기본_이미지로_설정한다(final String profileImageUrl) {
        // when
        final ProfileImageUrl actual = new ProfileImageUrl(profileImageUrl);

        // then
        assertThat(actual.getValue()).isEqualTo("");
    }
}
