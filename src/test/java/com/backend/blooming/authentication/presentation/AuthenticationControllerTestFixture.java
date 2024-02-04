package com.backend.blooming.authentication.presentation;

import com.backend.blooming.authentication.application.dto.LoginDto;
import com.backend.blooming.authentication.application.dto.LoginInformationDto;
import com.backend.blooming.authentication.application.dto.LogoutDto;
import com.backend.blooming.authentication.application.dto.TokenDto;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.authentication.presentation.dto.LogoutRequest;
import com.backend.blooming.authentication.presentation.dto.request.ReissueAccessTokenRequest;
import com.backend.blooming.authentication.presentation.dto.response.SocialLoginRequest;

@SuppressWarnings("NonAsciiCharacters")
public class AuthenticationControllerTestFixture {

    protected OAuthType oauth_타입 = OAuthType.KAKAO;
    protected String 소셜_액세스_토큰 = "social_access_token";
    protected String 디바이스_토큰 = "device_token";
    protected LoginDto 로그인_정보 = LoginDto.of(소셜_액세스_토큰, 디바이스_토큰);
    private String 유효하지_않은_소셜_액세스_토큰 = "social_access_token";
    protected LoginDto 유효하지_않은_소셜_액세스_토큰을_가진_로그인_정보 = LoginDto.of(유효하지_않은_소셜_액세스_토큰, 디바이스_토큰);
    protected SocialLoginRequest 소셜_로그인_정보 = new SocialLoginRequest(소셜_액세스_토큰, 디바이스_토큰);
    protected SocialLoginRequest 유효하지_않은_소셜_로그인_정보 = new SocialLoginRequest(유효하지_않은_소셜_액세스_토큰, 디바이스_토큰);
    protected LoginInformationDto 소셜_로그인_사용자_정보 = new LoginInformationDto(
            new TokenDto("access token", "refresh token"),
            true
    );
    protected LoginInformationDto 소셜_로그인_기존_사용자_정보 = new LoginInformationDto(
            new TokenDto("access token", "refresh token"),
            false
    );
    protected String 서비스_refresh_token = "blooming_refresh_token";
    protected ReissueAccessTokenRequest access_token_재발급_요청 = new ReissueAccessTokenRequest(서비스_refresh_token);
    protected TokenDto 서비스_토큰_정보 = new TokenDto(소셜_액세스_토큰, "refresh token");
    protected Long 사용자_아이디 = 1L;
    protected AuthClaims 사용자_토큰_정보 = new AuthClaims(사용자_아이디);
    protected LogoutRequest 로그아웃_정보_요청 = new LogoutRequest(서비스_refresh_token, 디바이스_토큰);
    protected LogoutDto 로그아웃_정보_dto = new LogoutDto(서비스_refresh_token, 디바이스_토큰);
}
