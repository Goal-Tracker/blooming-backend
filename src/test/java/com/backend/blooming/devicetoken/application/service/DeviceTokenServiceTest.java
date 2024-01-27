package com.backend.blooming.devicetoken.application.service;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.devicetoken.application.service.dto.ReadDeviceTokensDto;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DeviceTokenServiceTest extends DeviceTokenServiceTestFixture {

    @Autowired
    private DeviceTokenService deviceTokenService;

    @Test
    void 디바이스_토큰을_저장한다() {
        // when
        final Long actual = deviceTokenService.save(사용자_아이디, 디바이스_토큰);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 사용자의_디바이스_토큰_목록_조회() {
        // when
        final ReadDeviceTokensDto actual = deviceTokenService.readAllByUserId(사용자_아이디);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.deviceTokens()).hasSize(2);
            softAssertions.assertThat(actual.deviceTokens().get(0).id()).isEqualTo(디바이스_토큰1.getId());
            softAssertions.assertThat(actual.deviceTokens().get(0).deviceToken()).isEqualTo(디바이스_토큰1.getToken());
            softAssertions.assertThat(actual.deviceTokens().get(1).id()).isEqualTo(디바이스_토큰2.getId());
            softAssertions.assertThat(actual.deviceTokens().get(1).deviceToken()).isEqualTo(디바이스_토큰2.getToken());
        });
    }
}
