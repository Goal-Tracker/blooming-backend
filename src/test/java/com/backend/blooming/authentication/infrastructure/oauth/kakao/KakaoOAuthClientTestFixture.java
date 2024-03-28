package com.backend.blooming.authentication.infrastructure.oauth.kakao;

import com.backend.blooming.authentication.infrastructure.oauth.kakao.dto.KakaoUnlinkUserDto;
import com.backend.blooming.authentication.infrastructure.oauth.kakao.dto.KakaoUserInformationDto;
import org.springframework.beans.factory.annotation.Value;

@SuppressWarnings("NonAsciiCharacters")
public class KakaoOAuthClientTestFixture {

    @Value("${oauth.client.kakao.user-info-uri}")
    protected String 사용자_정보_요청_url;

    @Value("${oauth.client.kakao.user-unlink-uri}")
    protected String 사용자_연결_끊기_요청_url;

    @Value("${oauth.client.kakao.admin-key}")
    protected String 어드민_키;
    protected final String 사용자_ACCESS_TOKEN = "access_token";
    protected final String 사용자_유효하지_않은_ACCESS_TOKEN = "invalid_access_token";
    protected final String 사용자_요청_헤더 = "Bearer " + 사용자_ACCESS_TOKEN;
    protected final String 어드민_사용자_요청_헤더 = "ADMIN_TOKEN_TYPE " + 어드민_키;
    protected final String 사용자_유효하지_않은_요청_헤더 = "Bearer invalid_access_token";
    protected final String oAuthId = "12345";
    protected final KakaoUserInformationDto 사용자_정보 =
            new KakaoUserInformationDto(oAuthId, new KakaoUserInformationDto.KakaoAccount("test@email.com"));
    protected final String 요청_데이터 = "target_id_type=user_id&target_id=" + oAuthId;
    protected final KakaoUnlinkUserDto 연결을_끊은_사용자_정보 = new KakaoUnlinkUserDto(oAuthId);
}
