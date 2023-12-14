package com.backend.blooming.friend.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.friend.application.dto.ReadFriendsDto;
import com.backend.blooming.themecolor.domain.ThemeColor;

import java.util.List;

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

    private ReadFriendsDto.FriendDto.UserDto 사용자_정보_dto1 = new ReadFriendsDto.FriendDto.UserDto(2L, "12345", OAuthType.KAKAO.name(), "user1@email.com", "사용자1", ThemeColor.BLUE.getCode(), "상태 메시지");
    private ReadFriendsDto.FriendDto.UserDto 사용자_정보_dto2 = new ReadFriendsDto.FriendDto.UserDto(3L, "12346", OAuthType.KAKAO.name(), "user2@email.com", "사용자2", ThemeColor.BLUE.getCode(), "상태 메시지");
    protected ReadFriendsDto.FriendDto 친구_요청_정보_dto1 = new ReadFriendsDto.FriendDto(1L, 사용자_정보_dto1, false);
    protected ReadFriendsDto.FriendDto 친구_요청_정보_dto2 = new ReadFriendsDto.FriendDto(2L, 사용자_정보_dto2, false);
    protected ReadFriendsDto 친구_요청을_보낸_사용자들_정보_dto = new ReadFriendsDto(List.of(친구_요청_정보_dto1, 친구_요청_정보_dto2));
}
