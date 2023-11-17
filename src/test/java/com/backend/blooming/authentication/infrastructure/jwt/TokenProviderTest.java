package com.backend.blooming.authentication.infrastructure.jwt;

import com.backend.blooming.authentication.infrastructure.exception.InvalidTokenException;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.authentication.infrastructure.jwt.fixture.JwtTokenProviderTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TokenProviderTest extends JwtTokenProviderTestFixture {

    TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new TokenProvider(토큰_설정);
    }

    @Test
    void 액세스_토큰을_생성한다() {
        // when
        final String actual = tokenProvider.createToken(액세스_토큰_타입, 회원_아이디);

        // then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void 리프레시_토큰을_생성한다() {
        // when
        final String actual = tokenProvider.createToken(리프레시_토큰_타입, 회원_아이디);

        // then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void 유효한_토큰이라면_정보를_반환한다() {
        // given
        final String accessToken = 토큰_타입 + tokenProvider.createToken(액세스_토큰_타입, 회원_아이디);

        // when
        final AuthClaims actual = tokenProvider.parseToken(액세스_토큰_타입, accessToken);

        // then
        assertThat(actual.userId()).isEqualTo(회원_아이디);
    }

    @Test
    void 토큰_타입이_맞지_않는_토큰이라면_예외를_반환한다() {
        // when & then
        assertThatThrownBy(() -> tokenProvider.parseToken(액세스_토큰_타입, 타입이_맞지_않는_토큰))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Bearer 타입의 토큰이 아닙니다.");
    }

    @Test
    void 유효하지_않는_토큰이라면_예외를_반환한다() {
        // when & then
        assertThatThrownBy(() -> tokenProvider.parseToken(액세스_토큰_타입, 유효하지_않는_토큰))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("유효하지 않은 토큰 정보입니다.");
    }

    @Test
    void 기한이_만료된_토큰이라면_예외를_반환한다() {
        // given
        final TokenProvider tokenProvider = new TokenProvider(기한이_만료된_토큰_설정);
        final String accessToken = 토큰_타입 + tokenProvider.createToken(액세스_토큰_타입, 회원_아이디);

        // when & then
        assertThatThrownBy(() -> tokenProvider.parseToken(액세스_토큰_타입, accessToken))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("기한이 만료된 토큰입니다.");
    }
}
