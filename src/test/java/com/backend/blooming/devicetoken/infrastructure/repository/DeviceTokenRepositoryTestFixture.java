package com.backend.blooming.devicetoken.infrastructure.repository;

import com.backend.blooming.devicetoken.domain.DeviceToken;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class DeviceTokenRepositoryTestFixture {

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    protected Long 사용자_아이디 = 1L;
    protected DeviceToken 디바이스_토큰1;
    protected DeviceToken 디바이스_토큰2;
    protected DeviceToken 비활성화된_디바이스_토큰;

    @BeforeEach
    void setUpFixture() {
        디바이스_토큰1 = new DeviceToken(사용자_아이디, "token1");
        디바이스_토큰2 = new DeviceToken(사용자_아이디, "token2");
        비활성화된_디바이스_토큰 = new DeviceToken(사용자_아이디, "token3");
        비활성화된_디바이스_토큰.deactivate();

        deviceTokenRepository.saveAll(List.of(디바이스_토큰1, 디바이스_토큰2, 비활성화된_디바이스_토큰));
    }
}
