package com.backend.blooming.friend.application;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.friend.infrastructure.repository.FriendRepository;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class FriendServiceTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    protected Long 존재하지_않는_사용자_아이디 = 9999L;
    protected Long 사용자_아이디;
    protected Long 친구_요청할_사용자_아이디;
    protected Long 이미_친구_요청한_사용자;

    @BeforeEach
    void setUpFixture() {
        final User 사용자 = User.builder()
                             .oAuthId("12345")
                             .oAuthType(OAuthType.KAKAO)
                             .name("사용자1")
                             .email("user1@email.com")
                             .build();
        final User 친구_요청할_사용자 = User.builder()
                                    .oAuthId("12346")
                                    .oAuthType(OAuthType.KAKAO)
                                    .name("사용자2")
                                    .email("user2@email.com")
                                    .build();
        final User 이미_친구인_사용자 = User.builder()
                                    .oAuthId("12347")
                                    .oAuthType(OAuthType.KAKAO)
                                    .name("사용자3")
                                    .email("user3@email.com")
                                    .build();
        userRepository.saveAll(List.of(사용자, 친구_요청할_사용자, 이미_친구인_사용자));

        사용자_아이디 = 사용자.getId();
        친구_요청할_사용자_아이디 = 친구_요청할_사용자.getId();
        이미_친구_요청한_사용자 = 이미_친구인_사용자.getId();

        final Friend 친구 = new Friend(사용자, 이미_친구인_사용자);
        friendRepository.save(친구);
    }
}
