package com.backend.blooming.user.domain;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.themecolor.domain.ThemeColor;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserTest extends UserTestFixture {

    @Test
    void 사용자_생성시_색상_상태메시지를_설정하지_않을시_기본값으로_설정한다() {
        // when
        final User actual = User.builder()
                                .oAuthId("12345")
                                .oAuthType(OAuthType.KAKAO)
                                .email(new Email("user@email.com"))
                                .name(new Name("test"))
                                .build();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getOAuthId()).isEqualTo("12345");
            softAssertions.assertThat(actual.getOAuthType()).isEqualTo(OAuthType.KAKAO);
            softAssertions.assertThat(actual.getEmail()).isEqualTo("user@email.com");
            softAssertions.assertThat(actual.getName()).isEqualTo("test");
            softAssertions.assertThat(actual.getColor()).isEqualTo(ThemeColor.INDIGO);
            softAssertions.assertThat(actual.getStatusMessage()).isEqualTo("");
        });
    }

    @Test
    void 사용자를_삭제한다() {
        // when
        사용자.delete();

        // then
        assertThat(사용자.isDeleted()).isTrue();
    }

    @Test
    void 사용자의_이름을_수정한다() {
        // given
        final String updateName = "수정한 이름";

        // when
        사용자.updateName(new Name(updateName));

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(사용자.getOAuthId()).isEqualTo(기존_소셜_아이디);
            softAssertions.assertThat(사용자.getOAuthType()).isEqualTo(기존_소셜_타입);
            softAssertions.assertThat(사용자.getName()).isEqualTo(updateName);
            softAssertions.assertThat(사용자.getEmail()).isEqualTo(기존_이메일);
            softAssertions.assertThat(사용자.getColor()).isEqualTo(기존_테마_색상);
            softAssertions.assertThat(사용자.getStatusMessage()).isEqualTo(기존_상태_메시지);
        });
    }

    @Test
    void 사용자의_테마_색상을_수정한다() {
        // given
        final ThemeColor updateColor = ThemeColor.BLUE;

        // when
        사용자.updateColor(updateColor);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(사용자.getOAuthId()).isEqualTo(기존_소셜_아이디);
            softAssertions.assertThat(사용자.getOAuthType()).isEqualTo(기존_소셜_타입);
            softAssertions.assertThat(사용자.getName()).isEqualTo(기존_이름);
            softAssertions.assertThat(사용자.getEmail()).isEqualTo(기존_이메일);
            softAssertions.assertThat(사용자.getColor()).isEqualTo(updateColor);
            softAssertions.assertThat(사용자.getStatusMessage()).isEqualTo(기존_상태_메시지);
        });
    }

    @Test
    void 사용자의_상태_메시지를_수정한다() {
        // given
        final String updateStatusMessage = "수정한 상태 메시지";

        // when
        사용자.updateStatusMessage(updateStatusMessage);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(사용자.getOAuthId()).isEqualTo(기존_소셜_아이디);
            softAssertions.assertThat(사용자.getOAuthType()).isEqualTo(기존_소셜_타입);
            softAssertions.assertThat(사용자.getName()).isEqualTo(기존_이름);
            softAssertions.assertThat(사용자.getEmail()).isEqualTo(기존_이메일);
            softAssertions.assertThat(사용자.getColor()).isEqualTo(기존_테마_색상);
            softAssertions.assertThat(사용자.getStatusMessage()).isEqualTo(updateStatusMessage);
        });
    }

    @Test
    void 이메일_조회시_이메일_값을_반환한다() {
        // given
        final User user = User.builder()
                              .oAuthId("12345")
                              .oAuthType(OAuthType.KAKAO)
                              .name(new Name("사용자"))
                              .email(new Email("user@email.com"))
                              .color(ThemeColor.BEIGE)
                              .build();

        // when
        final String actual = user.getEmail();

        // then
        assertThat(actual).isEqualTo("user@email.com");
    }

    @Test
    void 테마_색상의_이름_조회시_해당_색상의_이름을_반환한다() {
        // given
        final User user = User.builder()
                              .oAuthId("12345")
                              .oAuthType(OAuthType.KAKAO)
                              .name(new Name("사용자"))
                              .email(new Email("user@email.com"))
                              .color(ThemeColor.BEIGE)
                              .build();

        // when
        final String actual = user.getColorName();

        // then
        assertThat(actual).isEqualTo(ThemeColor.BEIGE.name());
    }

    @Test
    void 테마_색상의_코드_조회시_해당_색상의_코드를_반환한다() {
        // given
        final User user = User.builder()
                              .oAuthId("12345")
                              .oAuthType(OAuthType.KAKAO)
                              .name(new Name("사용자"))
                              .email(new Email("user@email.com"))
                              .color(ThemeColor.BEIGE)
                              .build();

        // when
        final String actual = user.getColorCode();

        // then
        assertThat(actual).isEqualTo(ThemeColor.BEIGE.getCode());
    }
}
