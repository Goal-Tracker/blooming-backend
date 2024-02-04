package com.backend.blooming.devicetoken.application.service;

import com.backend.blooming.devicetoken.application.service.dto.ReadDeviceTokensDto;
import com.backend.blooming.devicetoken.domain.DeviceToken;
import com.backend.blooming.devicetoken.infrastructure.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;

    public Long saveOrActivate(final Long userId, final String token) {
        final DeviceToken deviceToken = findOrPersistDeviceToken(userId, token);
        activateIfInactive(deviceToken);

        return deviceToken.getId();
    }

    private DeviceToken findOrPersistDeviceToken(final Long userId, final String token) {
        return deviceTokenRepository.findByUserIdAndToken(userId, token)
                                    .orElseGet(() -> persistDeviceToken(userId, token));
    }

    private DeviceToken persistDeviceToken(final Long userId, final String token) {
        final DeviceToken deviceToken = new DeviceToken(userId, token);

        return deviceTokenRepository.save(deviceToken);
    }

    private void activateIfInactive(final DeviceToken deviceToken) {
        if (!deviceToken.isActive()) {
            deviceToken.activate();
        }
    }

    @Transactional(readOnly = true)
    public ReadDeviceTokensDto readAllByUserId(final Long userId) {
        final List<DeviceToken> deviceTokens = deviceTokenRepository.findAllByUserIdAndActiveIsTrue(userId);

        return ReadDeviceTokensDto.from(deviceTokens);
    }

    public void deactivate(final Long userId, final String token) {
        final Optional<DeviceToken> deviceToken = deviceTokenRepository.findByUserIdAndToken(userId, token);
        deviceToken.ifPresent(DeviceToken::deactivate);
    }

    public void deactivateAllByUserId(final Long userId) {
        final List<DeviceToken> deviceTokens = deviceTokenRepository.findAllByUserIdAndActiveIsTrue(userId);
        deviceTokens.forEach(DeviceToken::deactivate);
    }
}
