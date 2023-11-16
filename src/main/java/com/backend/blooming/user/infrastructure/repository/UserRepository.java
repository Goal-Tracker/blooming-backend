package com.backend.blooming.user.infrastructure.repository;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            SELECT u
            FROM User u
            WHERE u.oAuthId = :oAuthId AND u.oAuthType = :oAuthType
            """)
    Optional<User> findByOAuthIdAndOAuthType(final String oAuthId, final OAuthType oAuthType);

    boolean existsByIdAndDeletedIsFalse(final Long userId);
}

