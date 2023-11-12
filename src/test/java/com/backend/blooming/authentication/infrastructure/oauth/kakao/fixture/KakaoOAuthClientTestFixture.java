package com.backend.blooming.authentication.infrastructure.oauth.kakao.fixture;

import com.backend.blooming.authentication.infrastructure.oauth.kakao.dto.KakaoUserInformationDto;
import org.springframework.beans.factory.annotation.Value;

@SuppressWarnings("NonAsciiCharacters")
public class KakaoOAuthClientTestFixture {

    @Value("${oauth.client.kakao.user-info-uri}")
    protected String 사용자_정보_요청_url;
    protected final String 사용자_ACCESS_TOKEN = "access_token";
    protected final String 사용자_유효하지_않은_ACCESS_TOKEN = "invalid_access_token";
    protected final String 사용자_요청_헤더 = "Bearer access_token";
    protected final String 사용자_유효하지_않은_요청_헤더 = "Bearer invalid_access_token";
    protected final KakaoUserInformationDto 사용자_정보 =
            new KakaoUserInformationDto("oauthId", "name", "test@email.com");

}
