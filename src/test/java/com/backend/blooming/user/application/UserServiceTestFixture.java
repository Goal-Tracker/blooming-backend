package com.backend.blooming.user.application;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.friend.infrastructure.repository.FriendRepository;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.application.dto.UpdateUserDto;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.testcontainers.shaded.com.google.common.net.MediaType;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class UserServiceTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    protected User 사용자;
    protected User 친구인_사용자;
    protected User 친구가_아닌_사용자;
    protected Long 사용자_아이디;
    protected Long 삭제한_사용자_아아디;
    protected Long 존재하지_않는_사용자_아아디 = 9999L;

    protected String 검색_키워드 = "사용자";
    protected String 수정한_이름 = "수정한 이름";
    protected ThemeColor 수정한_테마_색상 = ThemeColor.BLUE;
    protected String 수정한_상태_메시지 = "수정한 상태 메시지";
    protected MockMultipartFile 수정한_프로필_이미지 = new MockMultipartFile(
            "profileImage",
            "image.png",
            MediaType.PNG.toString(),
            "image".getBytes()
    );
    protected String 기존_이름;
    protected ThemeColor 기존_테마_색상;
    protected String 기존_상태_메시지;
    protected String 기존_프로필_이미지_url;
    protected UpdateUserDto 모든_사용자_정보를_수정한_dto = new UpdateUserDto(수정한_이름, 수정한_테마_색상.name(), 수정한_상태_메시지, false, 수정한_프로필_이미지);
    protected UpdateUserDto 이름만_수정한_dto = new UpdateUserDto(수정한_이름, null, null, false, null);
    protected UpdateUserDto 기존_이름으로_수정한_dto;
    protected UpdateUserDto 이미_존재하는_이름으로_수정한_dto;
    protected UpdateUserDto 테마_색상만_수정한_dto = new UpdateUserDto(null, 수정한_테마_색상.name(), null, false, null);
    protected UpdateUserDto 상태_메시지만_수정한_dto = new UpdateUserDto(null, null, 수정한_상태_메시지, false, null);
    protected UpdateUserDto 프로필_이미지만_수정한_dto = new UpdateUserDto(null, null, null, false, 수정한_프로필_이미지);
    protected UpdateUserDto 기본_프로필_이미지로_수정한_dto = new UpdateUserDto(null, null, null, true, null);

    @BeforeEach
    void setUpFixture() {
        사용자 = User.builder()
                  .oAuthId("12345")
                  .oAuthType(OAuthType.KAKAO)
                  .name(new Name("사용자"))
                  .email(new Email("test@email.com"))
                  .color(ThemeColor.BEIGE)
                  .statusMessage("기존 상태 메시지")
                  .build();
        친구인_사용자 = User.builder()
                      .oAuthId("12346")
                      .oAuthType(OAuthType.KAKAO)
                      .name(new Name("사용자2"))
                      .email(new Email("test2@email.com"))
                      .build();
        친구가_아닌_사용자 = User.builder()
                         .oAuthId("12347")
                         .oAuthType(OAuthType.KAKAO)
                         .name(new Name("사용자3"))
                         .email(new Email("test3@email.com"))
                         .build();
        final User 삭제한_사용자 = User.builder()
                                 .oAuthId("12348")
                                 .oAuthType(OAuthType.KAKAO)
                                 .name(new Name("삭제한 사용자"))
                                 .email(new Email("test4@email.com"))
                                 .build();
        삭제한_사용자.delete();

        userRepository.saveAll(List.of(사용자, 친구인_사용자, 친구가_아닌_사용자, 삭제한_사용자));

        사용자_아이디 = 사용자.getId();
        삭제한_사용자_아아디 = 삭제한_사용자.getId();
        기존_이름 = 사용자.getName();
        기존_테마_색상 = 사용자.getColor();
        기존_상태_메시지 = 사용자.getStatusMessage();
        기존_프로필_이미지_url = 사용자.getProfileImageUrl();
        기존_이름으로_수정한_dto = new UpdateUserDto(사용자.getName(), null, null, false, null);
        이미_존재하는_이름으로_수정한_dto = new UpdateUserDto(친구인_사용자.getName(), null, null, false, null);

        final Friend 친구_요청 = new Friend(사용자, 친구인_사용자);
        친구_요청.acceptRequest();

        friendRepository.save(친구_요청);
    }
}
