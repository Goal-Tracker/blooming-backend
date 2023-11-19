package com.backend.blooming.authentication.infrastructure.jwt;

import com.backend.blooming.authentication.configuration.JwtPropertiesConfiguration;

@SuppressWarnings("NonAsciiCharacters")
public class JwtTokenProviderTestFixture {

    protected final JwtPropertiesConfiguration 토큰_설정 = new JwtPropertiesConfiguration(
            "testtesttesttesttesttesttesttesttesttestesttesttesttesttest",
            "testtesttesttesttesttesttesttesttesttestesttesttesttesttest",
            12L,
            1440L
    );
    protected final JwtPropertiesConfiguration 기한이_만료된_토큰_설정 = new JwtPropertiesConfiguration(
            "testtesttesttesttesttesttesttesttesttestesttesttesttesttest",
            "testtesttesttesttesttesttesttesttesttestesttesttesttesttest",
            0L,
            0L
    );
    protected final TokenType 액세스_토큰_타입 = TokenType.ACCESS;
    protected final TokenType 리프레시_토큰_타입 = TokenType.REFRESH;
    protected final Long 회원_아이디 = 1L;
    protected final String 토큰_타입 = "Bearer ";
    protected final String 타입이_맞지_않는_토큰 = "invalid_token_type";
    protected final String 유효하지_않는_토큰 = 토큰_타입 + "invalid_token";
}
