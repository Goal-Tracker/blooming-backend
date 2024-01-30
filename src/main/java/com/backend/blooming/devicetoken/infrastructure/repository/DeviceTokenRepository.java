package com.backend.blooming.devicetoken.infrastructure.repository;

import com.backend.blooming.devicetoken.domain.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    Optional<DeviceToken> findByUserIdAndToken(final Long userId, final String token);

    List<DeviceToken> findAllByUserIdAndActiveIsTrue(final Long userId);
}
