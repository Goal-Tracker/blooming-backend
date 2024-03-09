package com.backend.blooming.user.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.application.dto.ReadUserDto;
import com.backend.blooming.user.application.dto.ReadUsersWithFriendsStatusDto;
import com.backend.blooming.user.application.dto.UpdateUserDto;
import com.backend.blooming.user.infrastructure.repository.dto.FriendsStatus;
import com.backend.blooming.user.presentation.dto.request.UpdateUserRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class UserControllerTestFixture {

    @Autowired
    private ObjectMapper objectMapper;

    protected TokenType 액세스_토큰_타입 = TokenType.ACCESS;
    protected String 액세스_토큰 = "Bearer access_token";
    protected Long 사용자_아이디 = 1L;
    protected AuthClaims 사용자_토큰_정보 = new AuthClaims(사용자_아이디);
    protected String 검색어 = "사용자";
    protected ReadUserDto 사용자_정보_dto = new ReadUserDto(
            사용자_아이디,
            "12345",
            "KAKAO",
            "test@email.com",
            "사용자",
            "https://blooming.default.image.png",
            ThemeColor.BLUE.getCode(),
            "반갑습니다."
    );
    protected ReadUsersWithFriendsStatusDto.ReadUserWithFriendsStatusDto 사용자_정보_dto1 =
            new ReadUsersWithFriendsStatusDto.ReadUserWithFriendsStatusDto(
                    1L,
                    "12345",
                    OAuthType.KAKAO.name(),
                    "teat1@email.com",
                    "사용자1",
                    "https://blooming.default.image.png",
                    ThemeColor.BLUE.getCode(),
                    "상태 메시지",
                    FriendsStatus.SELF.name()
            );
    protected ReadUsersWithFriendsStatusDto.ReadUserWithFriendsStatusDto 사용자_정보_dto2 =
            new ReadUsersWithFriendsStatusDto.ReadUserWithFriendsStatusDto(
                    2L,
                    "12346",
                    OAuthType.KAKAO.name(),
                    "teat2@email.com",
                    "사용자2",
                    "https://blooming.default.image.png",
                    ThemeColor.CORAL.getCode(),
                    "상태 메시지",
                    FriendsStatus.FRIENDS.name()
            );
    protected ReadUsersWithFriendsStatusDto 검색어가_이름에_포함된_사용자들의_정보_dto = new ReadUsersWithFriendsStatusDto(
            List.of(사용자_정보_dto1, 사용자_정보_dto2)
    );

    protected MockMultipartFile 수정한_프로필_이미지 = new MockMultipartFile(
            "profileImage",
            "image.png",
            MediaType.IMAGE_PNG_VALUE,
            "image".getBytes()
    );
    protected MockMultipartFile 프로필_이미지가_null인_파일 = new MockMultipartFile("profileImage", (byte[]) null);
    protected UpdateUserDto 사용자의_모든_정보_수정_dto = new UpdateUserDto(
            "수정한 이름",
            ThemeColor.BLUE.name(),
            "수정한 상태 메시지",
            false,
            수정한_프로필_이미지
    );
    protected ReadUserDto 모든_정보가_수정된_사용자_정보_dto = new ReadUserDto(
            사용자_아이디,
            사용자_정보_dto.oAuthId(),
            사용자_정보_dto.oAuthType(),
            사용자_정보_dto.email(),
            사용자의_모든_정보_수정_dto.name(),
            "https://blooming.update.image.png",
            사용자의_모든_정보_수정_dto.color(),
            사용자의_모든_정보_수정_dto.statusMessage()
    );
    private UpdateUserRequest 사용자의_모든_정보_수정_요청_내용 = new UpdateUserRequest(
            사용자의_모든_정보_수정_dto.name(),
            사용자의_모든_정보_수정_dto.color(),
            사용자의_모든_정보_수정_dto.statusMessage(),
            false
    );
    protected MockMultipartFile 사용자의_모든_정보_수정_요청;
    protected UpdateUserDto 사용자의_테마_색상만_수정_dto = new UpdateUserDto(null, ThemeColor.BLUE.name(), null, false, 프로필_이미지가_null인_파일);
    protected ReadUserDto 테마_색상만_수정된_사용자_정보_dto = new ReadUserDto(
            사용자_아이디,
            사용자_정보_dto.oAuthId(),
            사용자_정보_dto.oAuthType(),
            사용자_정보_dto.email(),
            사용자_정보_dto.name(),
            사용자_정보_dto.profileImageUrl(),
            사용자의_테마_색상만_수정_dto.color(),
            사용자_정보_dto.statusMessage()
    );
    private UpdateUserRequest 사용자의_테마_색상만_수정_요청_내용 = new UpdateUserRequest(
            사용자의_테마_색상만_수정_dto.name(),
            사용자의_테마_색상만_수정_dto.color(),
            사용자의_테마_색상만_수정_dto.statusMessage(),
            false
    );
    protected MockMultipartFile 사용자의_테마_색상만_수정_요청;
    protected UpdateUserDto 사용자의_상태_메시지만_수정_dto = new UpdateUserDto(null, null, "수정한 상태 메시지", false, 프로필_이미지가_null인_파일);
    protected ReadUserDto 상태_메시지만_수정된_사용자_정보_dto = new ReadUserDto(
            사용자_아이디,
            사용자_정보_dto.oAuthId(),
            사용자_정보_dto.oAuthType(),
            사용자_정보_dto.email(),
            사용자_정보_dto.name(),
            사용자_정보_dto.profileImageUrl(),
            사용자_정보_dto.color(),
            사용자의_상태_메시지만_수정_dto.statusMessage()
    );
    private UpdateUserRequest 사용자의_상태_메시지만_수정_요청_내용 = new UpdateUserRequest(
            사용자의_상태_메시지만_수정_dto.name(),
            사용자의_상태_메시지만_수정_dto.color(),
            사용자의_상태_메시지만_수정_dto.statusMessage(),
            false
    );
    protected MockMultipartFile 사용자의_상태_메시지만_수정_요청;
    protected UpdateUserDto 사용자의_이름만_수정_dto = new UpdateUserDto("수정한 이름", null, null, false, 프로필_이미지가_null인_파일);
    protected ReadUserDto 이름만_수정된_사용자_정보_dto = new ReadUserDto(
            사용자_아이디,
            사용자_정보_dto.oAuthId(),
            사용자_정보_dto.oAuthType(),
            사용자_정보_dto.email(),
            사용자의_모든_정보_수정_dto.name(),
            사용자_정보_dto.profileImageUrl(),
            사용자_정보_dto.color(),
            사용자_정보_dto.statusMessage()
    );
    private UpdateUserRequest 사용자의_이름만_수정_요청_내용 = new UpdateUserRequest(
            사용자의_이름만_수정_dto.name(),
            사용자의_이름만_수정_dto.color(),
            사용자의_이름만_수정_dto.statusMessage(),
            false
    );
    protected MockMultipartFile 사용자의_이름만_수정_요청;

    protected UpdateUserDto 사용자의_프로필_이미지만_수정_dto = new UpdateUserDto(null, null, null, false, 수정한_프로필_이미지);
    protected ReadUserDto 프로필_이미지만_수정된_사용자_정보_dto = new ReadUserDto(
            사용자_아이디,
            사용자_정보_dto.oAuthId(),
            사용자_정보_dto.oAuthType(),
            사용자_정보_dto.email(),
            사용자_정보_dto.name(),
            "https://blooming.update.image.png",
            사용자_정보_dto.color(),
            사용자_정보_dto.statusMessage()
    );
    private UpdateUserRequest 사용자의_프로필_이미지만_수정_요청_내용 = new UpdateUserRequest(
            사용자의_프로필_이미지만_수정_dto.name(),
            사용자의_프로필_이미지만_수정_dto.color(),
            사용자의_프로필_이미지만_수정_dto.statusMessage(),
            false
    );
    protected MockMultipartFile 사용자의_프로필_이미지만_수정_요청;

    @BeforeEach
    void setUpFixture() throws JsonProcessingException {
        사용자의_모든_정보_수정_요청 = new MockMultipartFile(
                "userRequest",
                null,
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(사용자의_모든_정보_수정_요청_내용).getBytes(StandardCharsets.UTF_8)
        );
        사용자의_테마_색상만_수정_요청 = new MockMultipartFile(
                "userRequest",
                null,
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(사용자의_테마_색상만_수정_요청_내용).getBytes(StandardCharsets.UTF_8)
        );
        사용자의_상태_메시지만_수정_요청 = new MockMultipartFile(
                "userRequest",
                null,
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(사용자의_상태_메시지만_수정_요청_내용).getBytes(StandardCharsets.UTF_8)
        );
        사용자의_이름만_수정_요청 = new MockMultipartFile(
                "userRequest",
                null,
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(사용자의_이름만_수정_요청_내용).getBytes(StandardCharsets.UTF_8)
        );
        사용자의_프로필_이미지만_수정_요청 = new MockMultipartFile(
                "userRequest",
                null,
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(사용자의_프로필_이미지만_수정_요청_내용).getBytes(StandardCharsets.UTF_8)
        );
    }
}
