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
import java.util.Optional;

@DataJpaTest
@Import(JpaConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DeviceTokenRepositoryTest extends DeviceTokenRepositoryTestFixture {

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    @Test
    void 사용자의_특정_디바이스_토큰을_조회한다() {
        // when
        final Optional<DeviceToken> actual = deviceTokenRepository.findByUserIdAndToken(사용자_아이디, 디바이스_토큰.getToken());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get().getId()).isEqualTo(디바이스_토큰.getId());
            softAssertions.assertThat(actual.get().getUserId()).isEqualTo(디바이스_토큰.getUserId());
            softAssertions.assertThat(actual.get().getToken()).isEqualTo(디바이스_토큰.getToken());
            softAssertions.assertThat(actual.get().isActive()).isEqualTo(디바이스_토큰.isActive());
        });
    }

    @Test
    void 사용자의_활성화되어_있는_모든_디바이스_토큰_목록을_조회한다() {
        // when
        final List<DeviceToken> actual = deviceTokenRepository.findAllByUserIdAndActiveIsTrue(사용자_아이디);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).getId()).isEqualTo(디바이스_토큰1.getId());
            softAssertions.assertThat(actual.get(0).getToken()).isEqualTo(디바이스_토큰1.getToken());
            softAssertions.assertThat(actual.get(1).getId()).isEqualTo(디바이스_토큰2.getId());
            softAssertions.assertThat(actual.get(1).getToken()).isEqualTo(디바이스_토큰2.getToken());
            softAssertions.assertThat(actual).doesNotContain(비활성화된_디바이스_토큰);
        });
    }
}
