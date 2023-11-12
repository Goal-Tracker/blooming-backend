package com.backend.blooming.authentication.infrastructure.oauth;

import com.backend.blooming.authentication.infrastructure.exception.UnSupportedOAuthTypeException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OAuthTypeTest {

    @Test
    void 지원하는_소셜_로그인_타입이라면_해당_타입을_반환한다() {
        // given
        final String targetType = "kakao";

        // when
        final OAuthType actual = OAuthType.from(targetType);

        // then
        assertThat(actual).isEqualTo(OAuthType.KAKAO);
    }

    @Test
    void 지원하지_않는_소셜_로그인_타입이라면_예외를_반환한다() {
        // given
        final String invalidType = "invalidType";

        // when & then
        assertThatThrownBy(() -> OAuthType.from(invalidType))
                .isInstanceOf(UnSupportedOAuthTypeException.class)
                .hasMessage("지원하지 않는 소셜 로그인 방식입니다.");
    }
}
