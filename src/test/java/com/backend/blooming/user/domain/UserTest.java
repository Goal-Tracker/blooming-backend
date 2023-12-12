package com.backend.blooming.user.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserTest {

    @Test
    void 사용자를_생성한다() {
        // when
        final User actual = User.builder()
                              .oauthId("12345")
                              .email("test@email.com")
                              .name("사용자")
                              .color("#12345")
                              .statusMessage("상태 메시지")
                              .build();

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getOauthId()).isEqualTo("12345");
            softAssertions.assertThat(actual.getEmail()).isEqualTo("test@email.com");
            softAssertions.assertThat(actual.getName()).isEqualTo("사용자");
            softAssertions.assertThat(actual.getColor()).isEqualTo("#12345");
            softAssertions.assertThat(actual.getStatusMessage()).isEqualTo("상태 메시지");
        });
    }
}
