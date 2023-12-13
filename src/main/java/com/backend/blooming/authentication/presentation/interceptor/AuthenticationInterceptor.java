package com.backend.blooming.authentication.presentation.interceptor;

import com.backend.blooming.authentication.infrastructure.exception.InvalidTokenException;
import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedThreadLocal;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final AuthenticatedThreadLocal authenticatedThreadLocal;

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        final String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (isNotExist(accessToken)) {
            return true;
        }

        final AuthClaims authClaims = tokenProvider.parseToken(TokenType.ACCESS, accessToken);
        validateExistUser(authClaims.userId());
        authenticatedThreadLocal.set(authClaims);

        return true;
    }

    private boolean isNotExist(final String accessToken) {
        return accessToken == null || accessToken.isEmpty();
    }

    private void validateExistUser(final Long userId) {
        if (!userRepository.existsByIdAndDeletedIsFalse(userId)) {
            throw new InvalidTokenException();
        }
    }

    @Override
    public void afterCompletion(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler,
            final Exception ex
    ) {
        authenticatedThreadLocal.unset();
    }
}
