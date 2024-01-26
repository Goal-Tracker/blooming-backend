package com.backend.blooming.devicetoken.application.service;

import com.backend.blooming.devicetoken.domain.DeviceToken;
import com.backend.blooming.devicetoken.infrastructure.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;

    public Long save(final Long userId, final String token) {
        final DeviceToken deviceToken = new DeviceToken(userId, token);

        return deviceTokenRepository.save(deviceToken)
                                    .getId();
    }
}
