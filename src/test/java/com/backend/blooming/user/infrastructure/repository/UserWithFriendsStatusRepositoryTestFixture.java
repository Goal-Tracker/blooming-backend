package com.backend.blooming.user.infrastructure.repository;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.friend.infrastructure.repository.FriendRepository;
import com.backend.blooming.user.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class UserWithFriendsStatusRepositoryTestFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    protected User 사용자;
    protected User 친구인_사용자;
    protected User 친구로_요청한_사용자;
    protected User 친구_요청을_받은_사용자;
    protected User 관계가_없는_사용자;
    protected User 삭제된_사용자;

    @BeforeEach
    void setUpFixture() {
        사용자 = User.builder()
                  .oAuthId("12345")
                  .oAuthType(OAuthType.KAKAO)
                  .name("사용자")
                  .email("test@email.com")
                  .build();
        친구인_사용자 = User.builder()
                      .oAuthId("12346")
                      .oAuthType(OAuthType.KAKAO)
                      .name("사용자2 검색어")
                      .email("test2@email.com")
                      .build();
        친구로_요청한_사용자 = User.builder()
                          .oAuthId("12347")
                          .oAuthType(OAuthType.KAKAO)
                          .name("사용자3 검색어")
                          .email("test3@email.com")
                          .build();
        친구_요청을_받은_사용자 = User.builder()
                            .oAuthId("12348")
                            .oAuthType(OAuthType.KAKAO)
                            .name("사용자4")
                            .email("test4@email.com")
                            .build();
        관계가_없는_사용자 = User.builder()
                         .oAuthId("12349")
                         .oAuthType(OAuthType.KAKAO)
                         .name("사용자5")
                         .email("test5@email.com")
                         .build();
        삭제된_사용자 = User.builder()
                      .oAuthId("12350")
                      .oAuthType(OAuthType.KAKAO)
                      .name("사용자6")
                      .email("test6@email.com")
                      .build();
        삭제된_사용자.delete();
        userRepository.saveAll(List.of(사용자, 친구인_사용자, 친구로_요청한_사용자, 친구_요청을_받은_사용자, 관계가_없는_사용자, 삭제된_사용자));

        final Friend 친구인_요청 = new Friend(사용자, 친구인_사용자);
        final Friend 친구_요청만_한_요청 = new Friend(사용자, 친구로_요청한_사용자);
        final Friend 친구로_요청_받은_요청 = new Friend(친구_요청을_받은_사용자, 사용자);
        친구인_요청.acceptRequest();
        friendRepository.saveAll(List.of(친구인_요청, 친구_요청만_한_요청, 친구로_요청_받은_요청));

        em.flush();
        em.clear();
    }
}
