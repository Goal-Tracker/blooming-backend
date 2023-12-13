package com.backend.blooming.friend.domain;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
public class FriendTestFixture {

    protected User 친구_요청을_한_사용자 = User.builder()
                                      .oAuthId("12345")
                                      .oAuthType(OAuthType.KAKAO)
                                      .name("사용자1")
                                      .email("user1@email.com")
                                      .build();
    protected User 친구_요청을_받은_사용자 = User.builder()
                                       .oAuthId("12345")
                                       .oAuthType(OAuthType.KAKAO)
                                       .name("사용자1")
                                       .email("user1@email.com")
                                       .build();

    @BeforeEach
    void setUpFixture() {
        ReflectionTestUtils.setField(친구_요청을_한_사용자, "id", 1L);
        ReflectionTestUtils.setField(친구_요청을_받은_사용자, "id", 2L);
    }
}
