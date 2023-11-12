package com.backend.blooming.authentication.configuration;

import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
public record JwtPropertiesConfiguration(
        String accessTokenKey,
        String refreshTokenKey,
        Long accessExpireHour,
        Long refreshExpireHour
) {

    public String findTokenKey(final TokenType tokenType) {
        if (TokenType.ACCESS.equals(tokenType)) {
            return accessTokenKey;
        }

        return refreshTokenKey;
    }

    public Long findTokenExpireHour(final TokenType tokenType) {
        if (TokenType.ACCESS.equals(tokenType)) {
            return accessExpireHour;
        }

        return refreshExpireHour;
    }
}
