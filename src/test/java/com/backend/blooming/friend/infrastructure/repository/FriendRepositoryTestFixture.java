package com.backend.blooming.friend.infrastructure.repository;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class FriendRepositoryTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    protected User 친구_요청한_사용자;
    protected User 이미_친구_요청_받은_사용자;
    protected User 친구_요청을_받은_사용자;
    protected User 친구_요청을_받은적_없는_사용자;
    protected User 친구가_있는_사용자;
    protected Friend 보낸_친구_요청1;
    protected Friend 보낸_친구_요청2;
    protected Friend 보낸_친구_요청3;
    protected Friend 받은_친구_요청1;
    protected Friend 받은_친구_요청2;
    protected Friend 받은_친구_요청3;
    protected Friend 친구인_요청1;
    protected Friend 친구인_요청2;
    protected Friend 친구인_요청3;

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
        final User 친구_요청을_받은_사용자2 = User.builder()
                                        .oAuthId("12348")
                                        .oAuthType(OAuthType.KAKAO)
                                        .name("사용자4")
                                        .email("user4@email.com")
                                        .build();
        final User 친구_요청을_받은_사용자3 = User.builder()
                                        .oAuthId("12349")
                                        .oAuthType(OAuthType.KAKAO)
                                        .name("사용자5")
                                        .email("user5@email.com")
                                        .build();
        final User 친구인_사용자1 = User.builder()
                                  .oAuthId("23456")
                                  .oAuthType(OAuthType.KAKAO)
                                  .name("친구1")
                                  .email("friend1@email.com")
                                  .build();
        final User 친구인_사용자2 = User.builder()
                                  .oAuthId("23457")
                                  .oAuthType(OAuthType.KAKAO)
                                  .name("친구2")
                                  .email("friend2@email.com")
                                  .build();
        final User 친구인_사용자3 = User.builder()
                                  .oAuthId("23458")
                                  .oAuthType(OAuthType.KAKAO)
                                  .name("친구3")
                                  .email("friend3@email.com")
                                  .build();
        userRepository.saveAll(List.of(
                친구_요청한_사용자,
                이미_친구_요청_받은_사용자,
                친구_요청을_받은적_없는_사용자,
                친구_요청을_받은_사용자2,
                친구_요청을_받은_사용자3,
                친구인_사용자1,
                친구인_사용자2,
                친구인_사용자3
        ));

        보낸_친구_요청1 = new Friend(친구_요청한_사용자, 이미_친구_요청_받은_사용자);
        보낸_친구_요청2 = new Friend(친구_요청한_사용자, 친구_요청을_받은_사용자2);
        보낸_친구_요청3 = new Friend(친구_요청한_사용자, 친구_요청을_받은_사용자3);

        친구_요청을_받은_사용자 = 이미_친구_요청_받은_사용자;
        받은_친구_요청1 = 보낸_친구_요청1;
        받은_친구_요청2 = new Friend(친구_요청을_받은_사용자2, 친구_요청을_받은_사용자);
        받은_친구_요청3 = new Friend(친구_요청을_받은_사용자3, 친구_요청을_받은_사용자);

        친구가_있는_사용자 = 친구_요청을_받은_사용자3;
        친구인_요청1 = new Friend(친구_요청을_받은_사용자3, 친구인_사용자1);
        친구인_요청2 = new Friend(친구인_사용자2, 친구_요청을_받은_사용자3);
        친구인_요청3 = new Friend(친구_요청을_받은_사용자3, 친구인_사용자3);
        friendRepository.saveAll(List.of(
                보낸_친구_요청1,
                보낸_친구_요청2,
                보낸_친구_요청3,
                받은_친구_요청2,
                받은_친구_요청3,
                친구인_요청1,
                친구인_요청2,
                친구인_요청3
        ));
        친구인_요청1.acceptRequest();
        친구인_요청2.acceptRequest();
        친구인_요청3.acceptRequest();
    }
}
