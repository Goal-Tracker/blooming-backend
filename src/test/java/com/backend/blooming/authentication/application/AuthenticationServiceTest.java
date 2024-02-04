package com.backend.blooming.authentication.application;

import com.backend.blooming.authentication.application.dto.LoginInformationDto;
import com.backend.blooming.authentication.application.dto.TokenDto;
import com.backend.blooming.authentication.domain.BlackListToken;
import com.backend.blooming.authentication.infrastructure.blacklist.BlackListTokenRepository;
import com.backend.blooming.authentication.infrastructure.exception.InvalidTokenException;
import com.backend.blooming.authentication.infrastructure.exception.OAuthException;
import com.backend.blooming.authentication.infrastructure.exception.UnsupportedOAuthTypeException;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthClient;
import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.devicetoken.domain.DeviceToken;
import com.backend.blooming.devicetoken.infrastructure.repository.DeviceTokenRepository;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.assertj.core.api.SoftAssertions;
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
    private OAuthClient oAuthClient;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    @Autowired
    private BlackListTokenRepository blackListTokenRepository;

    @Test
    void 로그인시_존재하지_않는_사용자인_경우_해당_사용자를_저장후_토큰_정보를_반환한다() {
        // given
        willReturn(첫_로그인_사용자_소셜_정보).given(oAuthClient).findUserInformation(소셜_액세스_토큰);

        // when
        final LoginInformationDto actual = authenticationService.login(oauth_타입, 로그인_정보);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.token().accessToken()).isNotEmpty();
            softAssertions.assertThat(actual.token().refreshToken()).isNotEmpty();
            softAssertions.assertThat(actual.isSignUp()).isTrue();
        });
    }

    @Test
    void 로그인시_디바이스_토큰이_존재하지_않더라도_로그인에_실패하지_않는다() {
        // given
        willReturn(첫_로그인_사용자_소셜_정보).given(oAuthClient).findUserInformation(소셜_액세스_토큰);

        // when
        final LoginInformationDto actual = authenticationService.login(oauth_타입, 디바이스_토큰이_없는_로그인_정보);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.token().accessToken()).isNotEmpty();
            softAssertions.assertThat(actual.token().refreshToken()).isNotEmpty();
            softAssertions.assertThat(actual.isSignUp()).isTrue();
        });
    }

    @Test
    void 로그인시_존재하지_않는_사용자이며_oauthid가_50자를_초과하는_경우_이름을_50자까지만_저장한_후_토큰_정보를_반환한다() {
        // given
        willReturn(oauthid가_50자를_초과하는_사용자_소셜_정보).given(oAuthClient).findUserInformation(소셜_액세스_토큰);

        // when
        final LoginInformationDto actual = authenticationService.login(oauth_타입, 로그인_정보);

        // then
        final User savedUser = userRepository.findByOAuthIdAndOAuthType(
                                                     oauthid가_50자를_초과하는_사용자_소셜_정보.oAuthId(),
                                                     oauth_타입
                                             )
                                             .get();
        System.out.println(savedUser);

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.token().accessToken()).isNotEmpty();
            softAssertions.assertThat(actual.token().refreshToken()).isNotEmpty();
            softAssertions.assertThat(actual.isSignUp()).isTrue();
            softAssertions.assertThat(savedUser.getName().length()).isEqualTo(50);
        });
    }

    @Test
    void 로그인시_존재하는_사용자인_경우_사용자_정보를_반환시_회원가입_여부는_거짓이다() {
        // given
        willReturn(기존_사용자_소셜_정보).given(oAuthClient).findUserInformation(소셜_액세스_토큰);

        // when
        final LoginInformationDto actual = authenticationService.login(oauth_타입, 로그인_정보);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.token().accessToken()).isNotEmpty();
            softAssertions.assertThat(actual.token().refreshToken()).isNotEmpty();
            softAssertions.assertThat(actual.isSignUp()).isFalse();
        });
    }

    @Test
    void 로그인시_oauth_타입이_null인_경우_예외를_반환한다() {
        // when & then
        assertThatThrownBy(() -> authenticationService.login(null, 로그인_정보))
                .isInstanceOf(UnsupportedOAuthTypeException.class);
    }

    @Test
    void 로그인시_존재하지_않는_사용자이며_유효하지_않는_oauth_토큰인_경우_예외를_반환한다() {
        // given
        willThrow(OAuthException.class).given(oAuthClient).findUserInformation(소셜_액세스_토큰);

        // when & then
        assertThatThrownBy(() -> authenticationService.login(oauth_타입, 로그인_정보))
                .isInstanceOf(OAuthException.class);
    }

    @Test
    void 유효한_refresh_token으로_access_token_재발행시_새로운_access_token을_반환한다() {
        // when
        final TokenDto tokenDto = authenticationService.reissueAccessToken(유효한_refresh_token);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(tokenDto.accessToken()).isNotEmpty();
            softAssertions.assertThat(tokenDto.refreshToken()).isEqualTo(유효한_refresh_token);
        });
    }

    @Test
    void 존재하지_않는_사용자의_refresh_token으로_access_token_재발행시_예외를_반환한다() {
        // when & then
        assertThatThrownBy(() -> authenticationService.reissueAccessToken(존재하지_않는_사용자의_refresh_token))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void 유효하지_않는_refresh_token으로_access_token_재발행시_예외를_반환한다() {
        // when & then
        assertThatThrownBy(() -> authenticationService.reissueAccessToken(유효하지_않는_refresh_token))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void 유효하지_않는_타입의_refresh_token으로_access_token_재발행시_예외를_반환한다() {
        // when & then
        assertThatThrownBy(() -> authenticationService.reissueAccessToken(유효하지_않는_타입의_refresh_token))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void 로그아웃시_디바이스_토큰과_액세스_토큰을_비활성화한다() {
        // when
        authenticationService.logout(기존_사용자.getId(), 로그아웃_dto);

        // then
        final BlackListToken blackListToken = blackListTokenRepository.findByToken(로그아웃_dto.refreshToken()).get();
        final DeviceToken deviceToken = deviceTokenRepository.findByUserIdAndToken(
                기존_사용자.getId(),
                로그아웃_dto.deviceToken()
        ).get();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(blackListToken.getToken()).isEqualTo(로그아웃_dto.refreshToken());
            softAssertions.assertThat(deviceToken.isActive()).isFalse();
        });
    }

    @Test
    void 로그아웃시_리프레시_토큰이_유효하지_않다면_예외를_발생시킨다() {
        // when & then
        assertThatThrownBy(() -> authenticationService.logout(기존_사용자.getId(), 유효하지_않은_리프레시_토큰을_갖는_로그아웃_dto))
                .isInstanceOf(InvalidTokenException.class);
    }
}
