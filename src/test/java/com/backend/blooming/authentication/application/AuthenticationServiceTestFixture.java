package com.backend.blooming.authentication.application;

import com.backend.blooming.authentication.application.dto.LoginDto;
import com.backend.blooming.authentication.application.dto.LogoutDto;
import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.authentication.infrastructure.oauth.dto.UserInformationDto;
import com.backend.blooming.authentication.infrastructure.oauth.kakao.dto.KakaoUserInformationDto;
import com.backend.blooming.devicetoken.domain.DeviceToken;
import com.backend.blooming.devicetoken.infrastructure.repository.DeviceTokenRepository;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
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

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    protected OAuthType oauth_타입 = OAuthType.KAKAO;
    protected String 소셜_액세스_토큰 = "social_access_token";
    protected String 디바이스_토큰 = "device_token";
    protected LoginDto 로그인_정보 = LoginDto.of(소셜_액세스_토큰, 디바이스_토큰);
    protected LoginDto 디바이스_토큰이_없는_로그인_정보 = LoginDto.of(소셜_액세스_토큰, null);
    protected UserInformationDto 첫_로그인_사용자_소셜_정보 =
            new KakaoUserInformationDto("12345", new KakaoUserInformationDto.KakaoAccount("test@email.com"));
    protected UserInformationDto oauthid가_50자를_초과하는_사용자_소셜_정보 =
            new KakaoUserInformationDto(
                    "1234567890123456789012345678901234567890123456789012345",
                    new KakaoUserInformationDto.KakaoAccount("test2@email.com")
            );
    protected UserInformationDto 기존_사용자_소셜_정보 =
            new KakaoUserInformationDto("12346", new KakaoUserInformationDto.KakaoAccount("test3@email.com"));
    protected String 유효한_refresh_token;
    protected String 존재하지_않는_사용자의_refresh_token;
    protected String 유효하지_않는_refresh_token = "Bearer invalid_refresh_token";
    protected String 유효하지_않는_타입의_refresh_token = "refresh_token";
    protected User 기존_사용자;
    protected LogoutDto 로그아웃_dto;
    protected LogoutDto 유효하지_않은_리프레시_토큰을_갖는_로그아웃_dto;

    @BeforeEach
    void setUpFixture() {
        기존_사용자 = User.builder()
                     .oAuthType(oauth_타입)
                     .oAuthId(기존_사용자_소셜_정보.oAuthId())
                     .name(new Name("기존 사용자"))
                     .email(new Email(기존_사용자_소셜_정보.email()))
                     .build();
        userRepository.save(기존_사용자);

        final DeviceToken 기존_사용자의_디바이스_토큰 = new DeviceToken(기존_사용자.getId(), "default_user_device_token");
        deviceTokenRepository.save(기존_사용자의_디바이스_토큰);

        유효한_refresh_token = "Bearer " + tokenProvider.createToken(TokenType.REFRESH, 기존_사용자.getId());
        final String 유효하지_않은_refresh_token = "Bearer " + tokenProvider.createToken(TokenType.REFRESH, 999L);

        로그아웃_dto = new LogoutDto(유효한_refresh_token, 기존_사용자의_디바이스_토큰.getToken());
        유효하지_않은_리프레시_토큰을_갖는_로그아웃_dto = new LogoutDto(유효하지_않은_refresh_token, 기존_사용자의_디바이스_토큰.getToken());

        final long 존재하지_않는_사용자_아이디 = 9999L;
        존재하지_않는_사용자의_refresh_token = "Bearer " + tokenProvider.createToken(TokenType.REFRESH, 존재하지_않는_사용자_아이디);
    }
}
