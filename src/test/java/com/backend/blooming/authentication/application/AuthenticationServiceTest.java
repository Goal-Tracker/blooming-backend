package com.backend.blooming.authentication.application;

import com.backend.blooming.authentication.application.dto.LoginInformationDto;
import com.backend.blooming.authentication.application.fixture.AuthenticationServiceTestFixture;
import com.backend.blooming.authentication.infrastructure.exception.OAuthException;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthClient;
import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthenticationServiceTest extends AuthenticationServiceTestFixture {

    @SpyBean
    OAuthClient oAuthClient;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserRepository userRepository;

    @Test
    void 로그인시_존재하지_않는_사용자인_경우_해당_사용자를_저장후_토큰_정보를_반환한다() {
        // given
        willReturn(첫_로그인_사용자_소셜_정보).given(oAuthClient).findUserInformation(소셜_액세스_토큰);

        // when
        final LoginInformationDto actual = authenticationService.login(oauth_타입, 소셜_액세스_토큰);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.token().accessToken()).isNotEmpty();
            softAssertions.assertThat(actual.token().refreshToken()).isNotEmpty();
            softAssertions.assertThat(actual.isSignUp()).isTrue();
        });
    }

    @Test
    void 로그인시_존재하는_사용자인_경우_사용자_정보를_반환시_회원가입_여부는_거짓이다() {
        // given
        willReturn(기존_사용자_소셜_정보).given(oAuthClient).findUserInformation(소셜_액세스_토큰);

        // when
        final LoginInformationDto actual = authenticationService.login(oauth_타입, 소셜_액세스_토큰);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.token().accessToken()).isNotEmpty();
            softAssertions.assertThat(actual.token().refreshToken()).isNotEmpty();
            softAssertions.assertThat(actual.isSignUp()).isFalse();
        });
    }

    @Test
    void 로그인시_존재하지_않는_사용자이며_유효하지_않는_oauth_토큰인_경우_예외를_반환한다() {
        // given
        willThrow(OAuthException.class).given(oAuthClient).findUserInformation(소셜_액세스_토큰);

        // when & then
        assertThatThrownBy(() -> authenticationService.login(oauth_타입, 소셜_액세스_토큰))
                .isInstanceOf(OAuthException.class);
    }
}
