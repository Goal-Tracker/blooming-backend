package com.backend.blooming.friend.infrastructure.repository;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class FriendRepositoryTestFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    protected User 친구_요청한_사용자;
    protected User 이미_친구_요청_받은_사용자;
    protected User 친구_요청을_받은적_없는_사용자;

    @BeforeEach
    void setUpFixture() {
        친구_요청한_사용자 = User.builder()
                         .oAuthId("12345")
                         .oAuthType(OAuthType.KAKAO)
                         .name("사용자1")
                         .email("user1@email.com")
                         .build();
        이미_친구_요청_받은_사용자 = User.builder()
                              .oAuthId("12346")
                              .oAuthType(OAuthType.KAKAO)
                              .name("사용자2")
                              .email("user2@email.com")
                              .build();
        친구_요청을_받은적_없는_사용자 = User.builder()
                                .oAuthId("12347")
                                .oAuthType(OAuthType.KAKAO)
                                .name("사용자3")
                                .email("user3@email.com")
                                .build();
        userRepository.saveAll(List.of(친구_요청한_사용자, 이미_친구_요청_받은_사용자, 친구_요청을_받은적_없는_사용자));

        final Friend 친구_요청 = new Friend(친구_요청한_사용자, 이미_친구_요청_받은_사용자);
        friendRepository.save(친구_요청);

        em.flush();
        em.clear();
    }
}
