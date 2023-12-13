package com.backend.blooming.authentication.presentation.argumentresolver;

import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedThreadLocal {

    private final ThreadLocal<AuthClaims> authClaimsThreadLocal = new ThreadLocal<>();

    public void set(final AuthClaims authClaims) {
        authClaimsThreadLocal.set(authClaims);
    }

    public void unset() {
        authClaimsThreadLocal.remove();
    }

    public AuthClaims get() {
        return authClaimsThreadLocal.get();
    }
}
