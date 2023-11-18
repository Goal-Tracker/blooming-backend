package com.backend.blooming.user.presentation.fixture;

import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.user.application.dto.UpdateUserDto;
import com.backend.blooming.user.application.dto.UserDto;
import com.backend.blooming.user.presentation.request.UpdateUserRequest;

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
            "BEIGE",
            "반갑습니다."
    );

    protected UpdateUserDto 사용자의_모든_정보_수정_dto = new UpdateUserDto("수정한 이름", "BLUE", "수정한 상태 메시지");
    protected UserDto 모든_정보가_수정된_사용자_정보_dto = new UserDto(
            사용자_아이디,
            사용자_정보_dto.oAuthId(),
            사용자_정보_dto.oAuthType(),
            사용자_정보_dto.email(),
            사용자의_모든_정보_수정_dto.name(),
            사용자의_모든_정보_수정_dto.color(),
            사용자의_모든_정보_수정_dto.statusMessage()
    );
    protected UpdateUserRequest 사용자의_모든_정보_수정_요청 = new UpdateUserRequest(
            사용자의_모든_정보_수정_dto.name(),
            사용자의_모든_정보_수정_dto.color(),
            사용자의_모든_정보_수정_dto.statusMessage()
    );
    protected UpdateUserDto 사용자의_이름만_수정_dto = new UpdateUserDto("수정한 이름", null, null);
    protected UserDto 이름만_수정된_사용자_정보_dto = new UserDto(
            사용자_아이디,
            사용자_정보_dto.oAuthId(),
            사용자_정보_dto.oAuthType(),
            사용자_정보_dto.email(),
            사용자의_모든_정보_수정_dto.name(),
            사용자_정보_dto.color(),
            사용자_정보_dto.statusMessage()
    );
    protected UpdateUserRequest 사용자의_이름만_수정_요청 = new UpdateUserRequest(
            사용자의_이름만_수정_dto.name(),
            사용자의_이름만_수정_dto.color(),
            사용자의_이름만_수정_dto.statusMessage()
    );
    protected UpdateUserDto 사용자의_테마_색상만_수정_dto = new UpdateUserDto(null, "BLUE", null);
    protected UserDto 테마_색상만_수정된_사용자_정보_dto = new UserDto(
            사용자_아이디,
            사용자_정보_dto.oAuthId(),
            사용자_정보_dto.oAuthType(),
            사용자_정보_dto.email(),
            사용자_정보_dto.name(),
            사용자의_테마_색상만_수정_dto.color(),
            사용자_정보_dto.statusMessage()
    );
    protected UpdateUserRequest 사용자의_테마_색상만_수정_요청 = new UpdateUserRequest(
            사용자의_테마_색상만_수정_dto.name(),
            사용자의_테마_색상만_수정_dto.color(),
            사용자의_테마_색상만_수정_dto.statusMessage()
    );
    protected UpdateUserDto 사용자의_상태_메시지만_수정_dto = new UpdateUserDto(null, null, "수정한 상태 메시지");
    protected UserDto 상태_메시지만_수정된_사용자_정보_dto = new UserDto(
            사용자_아이디,
            사용자_정보_dto.oAuthId(),
            사용자_정보_dto.oAuthType(),
            사용자_정보_dto.email(),
            사용자_정보_dto.name(),
            사용자_정보_dto.color(),
            사용자의_상태_메시지만_수정_dto.statusMessage()
    );
    protected UpdateUserRequest 사용자의_상태_메시지만_수정_요청 = new UpdateUserRequest(
            사용자의_상태_메시지만_수정_dto.name(),
            사용자의_상태_메시지만_수정_dto.color(),
            사용자의_상태_메시지만_수정_dto.statusMessage()
    );
}
