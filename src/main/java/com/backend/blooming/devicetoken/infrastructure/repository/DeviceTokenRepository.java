package com.backend.blooming.devicetoken.infrastructure.repository;

import com.backend.blooming.devicetoken.domain.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    List<DeviceToken> readAllByUserIdAndActiveIsFalse(final Long userId);
}
