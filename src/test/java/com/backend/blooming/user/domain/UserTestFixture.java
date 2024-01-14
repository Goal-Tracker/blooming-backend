package com.backend.blooming.user.domain;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.themecolor.domain.ThemeColor;

@SuppressWarnings("NonAsciiCharacters")
public class UserTestFixture {

    protected final String 기존_소셜_아이디 = "12345";
    protected final OAuthType 기존_소셜_타입 = OAuthType.KAKAO;
    protected final String 기존_이름 = "기존 이름";
    protected final String 기존_이메일 = "test@email.com";
    protected final ThemeColor 기존_테마_색상 = ThemeColor.BEIGE;
    protected final String 기존_상태_메시지 = "기존 상태 메시지";
    protected User 사용자 = User.builder()
                             .oAuthId(기존_소셜_아이디)
                             .oAuthType(기존_소셜_타입)
                             .name(new Name(기존_이름))
                             .email(new Email(기존_이메일))
                             .color(기존_테마_색상)
                             .statusMessage(기존_상태_메시지)
                             .build();
}
