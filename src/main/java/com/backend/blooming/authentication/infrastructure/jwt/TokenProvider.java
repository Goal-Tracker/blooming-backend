package com.backend.blooming.authentication.infrastructure.jwt;

import com.backend.blooming.authentication.configuration.JwtPropertiesConfiguration;
import com.backend.blooming.authentication.infrastructure.exception.InvalidTokenException;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String USER_ID_CLAIM_KEY = "userId";
    private static final String TOKEN_TYPE = "Bearer ";

    private final JwtPropertiesConfiguration propertiesConfiguration;

    public String createToken(final TokenType tokenType, final Long userId) {
        final Claims claims = Jwts.claims().setSubject("user");
        claims.put(USER_ID_CLAIM_KEY, userId);

        final Date now = new Date();
        final String tokenKey = propertiesConfiguration.findTokenKey(tokenType);

        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(now)
                   .setExpiration(calculateExpireTime(tokenType, now))
                   .signWith(SignatureAlgorithm.HS256, tokenKey)
                   .compact();
    }

    private Date calculateExpireTime(final TokenType tokenType, final Date now) {
        final Long expireHour = propertiesConfiguration.findTokenExpireHour(tokenType);
        final Long expireTime = expireHour * 60 * 60 * 1000L;

        return new Date(now.getTime() + expireTime);
    }

    public AuthClaims parseToke(final TokenType tokenType, final String token) {
        validateToken(token);

        final String pureToken = parsePureToken(token);
        final Claims claims = parseClaims(tokenType, pureToken);
        final Long userId = claims.get(USER_ID_CLAIM_KEY, Long.class);

        return new AuthClaims(userId);
    }

    private String parsePureToken(final String token) {
        return token.substring(TOKEN_TYPE.length());
    }

    private void validateToken(final String token) {
        if (!token.startsWith(TOKEN_TYPE)) {
            throw new InvalidTokenException("Bearer 타입의 토큰이 아닙니다.");
        }
    }

    private Claims parseClaims(final TokenType tokenType, final String token) {
        final String tokenKey = propertiesConfiguration.findTokenKey(tokenType);

        try {
            return Jwts.parserBuilder()
                       .setSigningKey(tokenKey)
                       .build()
                       .parseClaimsJws(token)
                       .getBody();
        } catch (final ExpiredJwtException exception) {
            throw new InvalidTokenException("기한이 만료된 토큰입니다.");
        } catch (final JwtException | IllegalArgumentException exception) {
            throw new InvalidTokenException("유효하지 않은 토큰 정보입니다.");
        }
    }
}
