package com.backend.blooming.authentication.application;

import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.authentication.infrastructure.oauth.dto.UserInformationDto;
import com.backend.blooming.authentication.infrastructure.oauth.kakao.dto.KakaoUserInformationDto;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class AuthenticationServiceTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    protected OAuthType oauth_타입 = OAuthType.KAKAO;
    protected String 소셜_액세스_토큰 = "social_access_token";
    protected UserInformationDto 첫_로그인_사용자_소셜_정보 =
            new KakaoUserInformationDto("12345", new KakaoUserInformationDto.KakaoAccount("test@email.com"));
    protected UserInformationDto 기존_사용자_소셜_정보 =
            new KakaoUserInformationDto("12346", new KakaoUserInformationDto.KakaoAccount("test2@email.com"));
    protected String 유효한_refresh_token;
    protected String 존재하지_않는_사용자의_refresh_token;
    protected String 유효하지_않는_refresh_token = "Bearer invalid_refresh_token";
    protected String 유효하지_않는_타입의_refresh_token = "refresh_token";

    @BeforeEach
    void setUpFixture() {
        final User 기존_사용자 = User.builder()
                                .oAuthType(oauth_타입)
                                .oAuthId(기존_사용자_소셜_정보.oAuthId())
                                .name("기존 사용자")
                                .email(new Email(기존_사용자_소셜_정보.email()))
                                .build();

        userRepository.save(기존_사용자);

        유효한_refresh_token = "Bearer " + tokenProvider.createToken(TokenType.REFRESH, 기존_사용자.getId());

        final long 존재하지_않는_사용자_아이디 = 9999L;
        존재하지_않는_사용자의_refresh_token = "Bearer " + tokenProvider.createToken(TokenType.REFRESH, 존재하지_않는_사용자_아이디);
    }
}
