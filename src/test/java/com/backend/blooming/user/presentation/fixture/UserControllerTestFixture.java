package com.backend.blooming.user.presentation.fixture;

import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.user.application.dto.UserDto;

@SuppressWarnings("NonAsciiCharacters")
public class UserControllerTestFixture {

    protected TokenType 액세스_토큰_타입 = TokenType.ACCESS;
    protected String 액세스_토큰 = "Bearer access_token";
    protected Long 사용자_아이디 = 1L;
    protected AuthClaims 사용자_토큰_정보 = new AuthClaims(사용자_아이디);
    protected UserDto 사용자_정보_dto = new UserDto(
            사용자_아이디,
            "12345",
            "KAKAO",
            "test@email.com",
            "사용자",
            "",
            "반갑습니다."
    );
}
