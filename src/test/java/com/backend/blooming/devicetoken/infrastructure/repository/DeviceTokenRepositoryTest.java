package com.backend.blooming.devicetoken.infrastructure.repository;

import com.backend.blooming.configuration.JpaConfiguration;
import com.backend.blooming.devicetoken.domain.DeviceToken;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@Import(JpaConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DeviceTokenRepositoryTest extends DeviceTokenRepositoryTestFixture {

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    @Test
    void 사용자의_삭제되지_않은_모든_디바이스_토큰_목록을_조회한다() {
        // when
        final List<DeviceToken> actual = deviceTokenRepository.readAllByUserIdAndDeletedIsFalse(사용자_아이디);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).getId()).isEqualTo(디바이스_토큰1.getId());
            softAssertions.assertThat(actual.get(0).getToken()).isEqualTo(디바이스_토큰1.getToken());
            softAssertions.assertThat(actual.get(1).getId()).isEqualTo(디바이스_토큰2.getId());
            softAssertions.assertThat(actual.get(1).getToken()).isEqualTo(디바이스_토큰2.getToken());
            softAssertions.assertThat(actual).doesNotContain(삭제된_디바이스_토큰);
        });
    }
}
