package com.backend.blooming.devicetoken.application.service;

import com.backend.blooming.configuration.IsolateDatabase;
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
}
