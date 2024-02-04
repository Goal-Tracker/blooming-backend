package com.backend.blooming.devicetoken.application.service;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.devicetoken.application.service.dto.ReadDeviceTokensDto;
import com.backend.blooming.devicetoken.domain.DeviceToken;
import com.backend.blooming.devicetoken.infrastructure.repository.DeviceTokenRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DeviceTokenServiceTest extends DeviceTokenServiceTestFixture {

    @Autowired
    private DeviceTokenService deviceTokenService;

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    @Test
    void 디바이스_토큰을_저장한다() {
        // when
        final Long actual = deviceTokenService.saveOrActivate(사용자_아이디, 디바이스_토큰);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 디바이스_토큰_저장시_비활성화된_동일한_토큰이_있다면_활성화한다() {
        // when
        final Long actual = deviceTokenService.saveOrActivate(사용자_아이디, 비활성화_디바이스_토큰.getToken());

        // then
        final DeviceToken deviceToken = deviceTokenRepository.findById(actual).get();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPositive();
            softAssertions.assertThat(deviceToken.getToken()).isEqualTo(비활성화_디바이스_토큰.getToken());
            softAssertions.assertThat(deviceToken.isActive()).isTrue();
        });
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

    @Test
    void 사용자의_특정_디바이스_토큰을_비활성화한다() {
        // when
        deviceTokenService.deactivate(사용자_아이디, 디바이스_토큰1.getToken());

        // then
        final DeviceToken actual = deviceTokenRepository.findByUserIdAndToken(사용자_아이디, 디바이스_토큰1.getToken()).get();
        assertThat(actual.isActive()).isFalse();
    }

    @Test
    void 사용자의_모든_디바이스_토큰을_비활성화한다() {
        // when
        deviceTokenService.deactivateAllByUserId(사용자_아이디);

        // then
        final List<DeviceToken> deviceTokens = deviceTokenRepository.findAllByUserIdAndActiveIsTrue(사용자_아이디);
        assertThat(deviceTokens).isEmpty();
    }
}
