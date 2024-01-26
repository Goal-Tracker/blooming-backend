package com.backend.blooming.devicetoken.infrastructure.repository;

import com.backend.blooming.devicetoken.domain.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
}
