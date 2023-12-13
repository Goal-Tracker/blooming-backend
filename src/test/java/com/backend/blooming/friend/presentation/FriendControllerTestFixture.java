package com.backend.blooming.friend.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;

@SuppressWarnings("NonAsciiCharacters")
public class FriendControllerTestFixture {

    protected TokenType 액세스_토큰_타입 = TokenType.ACCESS;
    protected String 액세스_토큰 = "Bearer access_token";
    protected Long 사용자_아이디 = 1L;
    protected AuthClaims 사용자_토큰_정보 = new AuthClaims(사용자_아이디);
    protected Long 친구_요청을_받은_사용자_아이디 = 2L;
    protected AuthClaims 친구_요청을_받은_사용자_토큰_정보 = new AuthClaims(친구_요청을_받은_사용자_아이디);
    protected Long 친구_요청_아이디 = 1L;
    protected Long 존재하지_않는_사용자_아이다 = 9999L;
    protected Long 이미_친구인_사용자_아이디 = 3L;
    protected Long 존재하지_않는_친구_요청_아이디 = 9999L;
    protected Long 친구_요청을_받지_않은_사용자_아이디 = 4L;
    protected AuthClaims 친구_요청을_받지_않은_사용자_토큰_정보 = new AuthClaims(친구_요청을_받지_않은_사용자_아이디);
}
