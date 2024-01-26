package com.backend.blooming.devicetoken.application.service.dto;

import com.backend.blooming.devicetoken.domain.DeviceToken;

import java.util.List;

public record ReadDeviceTokensDto(List<ReadDeviceTokenDto> deviceTokens) {

    public static ReadDeviceTokensDto from(final List<DeviceToken> deviceTokens) {
        final List<ReadDeviceTokenDto> deviceTokenDtos = deviceTokens.stream()
                                                                     .map(ReadDeviceTokenDto::from)
                                                                     .toList();

        return new ReadDeviceTokensDto(deviceTokenDtos);
    }

    public record ReadDeviceTokenDto(Long id, Long userId, String deviceToken) {

        public static ReadDeviceTokenDto from(final DeviceToken deviceToken) {
            return new ReadDeviceTokenDto(deviceToken.getId(), deviceToken.getUserId(), deviceToken.getToken());
        }
    }
}
