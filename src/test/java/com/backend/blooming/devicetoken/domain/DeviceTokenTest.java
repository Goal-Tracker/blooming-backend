package com.backend.blooming.devicetoken.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DeviceTokenTest {

    @Test
    void 디바이스_토큰을_활성화한다() {
        // given
        final DeviceToken deviceToken = new DeviceToken(1L, "token");
        deviceToken.deactivate();

        // when
        deviceToken.activate();

        // then
        assertThat(deviceToken.isActive()).isTrue();
    }

    @Test
    void 디바이스_토큰을_비활성화한다() {
        // given
        final DeviceToken deviceToken = new DeviceToken(1L, "token");

        // when
        deviceToken.deactivate();

        // then
        assertThat(deviceToken.isActive()).isFalse();
    }
}
