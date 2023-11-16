package com.backend.blooming.authentication.presentation.fixture;

import com.backend.blooming.authentication.application.dto.LoginInformationDto;
import com.backend.blooming.authentication.application.dto.TokenDto;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.authentication.presentation.response.SocialLoginRequest;

@SuppressWarnings("NonAsciiCharacters")
public class AuthenticationControllerTestFixture {

    protected OAuthType oauth_타입 = OAuthType.KAKAO;
    protected String 소셜_액세스_토큰 = "social_access_token";
    protected String 유효하지_않은_소셜_액세스_토큰 = "social_access_token";
    protected SocialLoginRequest 소셜_로그인_정보 = new SocialLoginRequest(소셜_액세스_토큰);
    protected SocialLoginRequest 유효하지_않은_소셜_로그인_정보 = new SocialLoginRequest(유효하지_않은_소셜_액세스_토큰);
    protected LoginInformationDto 소셜_로그인_사용자_정보 = new LoginInformationDto(
            new TokenDto("access token", "refresh token"),
            true
    );
    protected LoginInformationDto 소셜_로그인_기존_사용자_정보 = new LoginInformationDto(
            new TokenDto("access token", "refresh token"),
            false
    );
}
