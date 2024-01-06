package com.backend.blooming.friend.domain;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
public class FriendTestFixture {

    protected static User 친구_요청을_한_사용자 = User.builder()
                                             .oAuthId("12345")
                                             .oAuthType(OAuthType.KAKAO)
                                             .name("사용자1")
                                             .email(new Email("user1@email.com"))
                                             .build();
    protected static User 친구_요청을_받은_사용자 = User.builder()
                                              .oAuthId("12346")
                                              .oAuthType(OAuthType.KAKAO)
                                              .name("사용자2")
                                              .email(new Email("user2@email.com"))
                                              .build();
    protected static User 친구_요청과_상관없는_사용자 = User.builder()
                                                .oAuthId("12347")
                                                .oAuthType(OAuthType.KAKAO)
                                                .name("사용자3")
                                                .email(new Email("user3@email.com"))
                                                .build();

    @BeforeEach
    void setUpFixture() {
        ReflectionTestUtils.setField(친구_요청을_한_사용자, "id", 1L);
        ReflectionTestUtils.setField(친구_요청을_받은_사용자, "id", 2L);
        ReflectionTestUtils.setField(친구_요청과_상관없는_사용자, "id", 3L);
    }
}
