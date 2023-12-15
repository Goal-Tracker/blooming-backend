package com.backend.blooming.friend.application;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.friend.application.dto.ReadRequestFriendsDto;
import com.backend.blooming.friend.application.dto.ReadRequestedFriendsDto;
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
    protected Long 아직_친구_요청_전의_사용자_아이디;
    protected Long 이미_친구_요청을_받은_사용자_아이디;
    protected Long 친구_요청_아이디;
    protected Long 존재하지_않는_친구_요청_아이디 = 9999L;
    protected Long 친구_요청을_받지_않은_사용자_아이디;
    protected Long 친구_요청을_보낸_사용자_아이디;
    protected Long 친구_요청을_받은_사용자_아이디;
    protected ReadRequestedFriendsDto.FriendDto 친구_요청_사용자_정보_dto1;
    protected ReadRequestedFriendsDto.FriendDto 친구_요청_사용자_정보_dto2;
    protected ReadRequestedFriendsDto.FriendDto 친구_요청_사용자_정보_dto3;
    protected ReadRequestFriendsDto.FriendDto 친구_요청을_받은_사용자_정보_dto1;
    protected ReadRequestFriendsDto.FriendDto 친구_요청을_받은_사용자_정보_dto2;
    protected ReadRequestFriendsDto.FriendDto 친구_요청을_받은_사용자_정보_dto3;

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
        final User 친구_요청을_보낸_사용자 = User.builder()
                                       .oAuthId("12347")
                                       .oAuthType(OAuthType.KAKAO)
                                       .name("사용자3")
                                       .email("user3@email.com")
                                       .build();
        final User 이미_친구_요청을_받은_사용자 = User.builder()
                                          .oAuthId("12348")
                                          .oAuthType(OAuthType.KAKAO)
                                          .name("사용자4")
                                          .email("user4@email.com")
                                          .build();
        final User 친구_요청을_받은_사용자2 = User.builder()
                                        .oAuthId("12349")
                                        .oAuthType(OAuthType.KAKAO)
                                        .name("사용자5")
                                        .email("user5@email.com")
                                        .build();
        final User 친구_요청을_받은_사용자3 = User.builder()
                                        .oAuthId("12350")
                                        .oAuthType(OAuthType.KAKAO)
                                        .name("사용자6")
                                        .email("user6@email.com")
                                        .build();
        userRepository.saveAll(List.of(
                사용자,
                친구_요청할_사용자,
                이미_친구_요청을_받은_사용자,
                친구_요청을_보낸_사용자,
                친구_요청을_받은_사용자2,
                친구_요청을_받은_사용자3
        ));

        사용자_아이디 = 사용자.getId();
        아직_친구_요청_전의_사용자_아이디 = 친구_요청할_사용자.getId();
        이미_친구_요청을_받은_사용자_아이디 = 이미_친구_요청을_받은_사용자.getId();
        친구_요청을_받지_않은_사용자_아이디 = 아직_친구_요청_전의_사용자_아이디;
        친구_요청을_보낸_사용자_아이디 = 친구_요청을_보낸_사용자.getId();

        final Friend 친구_요청 = new Friend(사용자, 이미_친구_요청을_받은_사용자);
        final Friend 보낸_친구_요청1 = new Friend(친구_요청을_보낸_사용자, 이미_친구_요청을_받은_사용자);
        final Friend 보낸_친구_요청2 = new Friend(친구_요청을_보낸_사용자, 친구_요청을_받은_사용자2);
        final Friend 보낸_친구_요청3 = new Friend(친구_요청을_보낸_사용자, 친구_요청을_받은_사용자3);
        final Friend 받은_친구_요청1 = new Friend(이미_친구_요청을_받은_사용자, 사용자);
        final Friend 받은_친구_요청2 = new Friend(친구_요청을_받은_사용자2, 사용자);
        final Friend 받은_친구_요청3 = new Friend(친구_요청을_받은_사용자3, 사용자);
        friendRepository.saveAll(List.of(친구_요청, 보낸_친구_요청1, 보낸_친구_요청2, 보낸_친구_요청3, 받은_친구_요청1, 받은_친구_요청2, 받은_친구_요청3));

        친구_요청_아이디 = 친구_요청.getId();
        친구_요청을_받은_사용자_아이디 = 사용자.getId();

        final ReadRequestedFriendsDto 요청한_친구_목록_dto = ReadRequestedFriendsDto.from(List.of(보낸_친구_요청1, 보낸_친구_요청2, 보낸_친구_요청3));
        친구_요청_사용자_정보_dto1 = 요청한_친구_목록_dto.friends().get(0);
        친구_요청_사용자_정보_dto2 = 요청한_친구_목록_dto.friends().get(1);
        친구_요청_사용자_정보_dto3 = 요청한_친구_목록_dto.friends().get(2);

        final ReadRequestFriendsDto 요청_받은_친구_목록_dto = ReadRequestFriendsDto.from(List.of(받은_친구_요청1, 받은_친구_요청2, 받은_친구_요청3));
        친구_요청을_받은_사용자_정보_dto1 = 요청_받은_친구_목록_dto.friends().get(0);
        친구_요청을_받은_사용자_정보_dto2 = 요청_받은_친구_목록_dto.friends().get(1);
        친구_요청을_받은_사용자_정보_dto3 = 요청_받은_친구_목록_dto.friends().get(2);
    }
}
