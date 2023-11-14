package com.backend.blooming.authentication.application.fixture;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.authentication.infrastructure.oauth.dto.UserInformationDto;
import com.backend.blooming.authentication.infrastructure.oauth.kakao.dto.KakaoUserInformationDto;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class AuthenticationServiceTestFixture {

    @Autowired
    private UserRepository userRepository;

    protected OAuthType oauth_타입 = OAuthType.KAKAO;
    protected String 소셜_액세스_토큰 = "social_access_token";
    protected UserInformationDto 첫_로그인_사용자_소셜_정보 =
            new KakaoUserInformationDto("12345", "name", "test@email.com");
    protected UserInformationDto 기존_사용자_소셜_정보 =
            new KakaoUserInformationDto("12346", "name2", "test2@email.com");

    @BeforeEach
    void setUpFixture() {
        final User 기존_사용자 = User.builder()
                                .oAuthType(oauth_타입)
                                .oAuthId(기존_사용자_소셜_정보.oAuthId())
                                .name(기존_사용자_소셜_정보.nickname())
                                .email(기존_사용자_소셜_정보.email())
                                .build();

        userRepository.save(기존_사용자);
    }
}
