package com.backend.blooming.notification.application;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.friend.infrastructure.repository.FriendRepository;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class NotificationServiceTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    protected User 친구_요청을_보낸_사용자;
    protected User 친구_요청을_받은_사용자;
    protected Friend 보낸_친구_요청;

    @BeforeEach
    void setUpFixture() {
        친구_요청을_보낸_사용자 = User.builder()
                            .oAuthId("12345")
                            .oAuthType(OAuthType.KAKAO)
                            .name(new Name("사용자1"))
                            .email(new Email("user1@email.com"))
                            .build();
        친구_요청을_받은_사용자 = User.builder()
                            .oAuthId("12346")
                            .oAuthType(OAuthType.KAKAO)
                            .name(new Name("사용자2"))
                            .email(new Email("user2@email.com"))
                            .build();

        userRepository.saveAll(List.of(친구_요청을_보낸_사용자, 친구_요청을_받은_사용자));

        보낸_친구_요청 = new Friend(친구_요청을_보낸_사용자, 친구_요청을_받은_사용자);
        friendRepository.save(보낸_친구_요청);
    }
}
