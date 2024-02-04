package com.backend.blooming.authentication.infrastructure.blacklist;

import com.backend.blooming.authentication.domain.BlackListToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlackListTokenRepository extends JpaRepository<BlackListToken, Long> {

    Optional<BlackListToken> findByToken(final String token);
}
