package com.backend.blooming.notification.application;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.friend.infrastructure.repository.FriendRepository;
import com.backend.blooming.notification.domain.Notification;
import com.backend.blooming.notification.infrastructure.repository.NotificationRepository;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.backend.blooming.notification.domain.NotificationType.REQUEST_FRIEND;

@SuppressWarnings("NonAsciiCharacters")
public class NotificationServiceTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    protected Friend 보낸_친구_요청;
    protected User 친구_요청을_보낸_사용자;
    protected User 친구_요청을_받은_사용자;
    protected Long 사용자_아이디;
    protected Long 존재하지_않는_사용자 = 999L;
    protected Notification 친구_요청_알림1;
    protected Notification 친구_요청_알림2;

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
        final User 친구_요청을_보낸_사용자1 = User.builder()
                                        .oAuthId("12347")
                                        .oAuthType(OAuthType.KAKAO)
                                        .name(new Name("사용자3"))
                                        .email(new Email("user3@email.com"))
                                        .build();
        final User 친구_요청을_보낸_사용자2 = User.builder()
                                        .oAuthId("12348")
                                        .oAuthType(OAuthType.KAKAO)
                                        .name(new Name("사용자4"))
                                        .email(new Email("user4@email.com"))
                                        .build();

        userRepository.saveAll(List.of(친구_요청을_보낸_사용자, 친구_요청을_받은_사용자, 친구_요청을_보낸_사용자1, 친구_요청을_보낸_사용자2));

        보낸_친구_요청 = new Friend(친구_요청을_보낸_사용자, 친구_요청을_받은_사용자);

        final User 알림이_있는_사용자 = 친구_요청을_보낸_사용자;
        사용자_아이디 = 알림이_있는_사용자.getId();
        final Friend 보낸_친구_요청1 = new Friend(친구_요청을_보낸_사용자1, 알림이_있는_사용자);
        final Friend 보낸_친구_요청2 = new Friend(친구_요청을_보낸_사용자2, 알림이_있는_사용자);

        friendRepository.saveAll(List.of(보낸_친구_요청, 보낸_친구_요청1, 보낸_친구_요청2));

        친구_요청_알림1 = Notification.builder()
                                .receiver(알림이_있는_사용자)
                                .title(REQUEST_FRIEND.getTitle())
                                .content(REQUEST_FRIEND.getContentByFormat(친구_요청을_보낸_사용자1.getName()))
                                .type(REQUEST_FRIEND)
                                .requestId(친구_요청을_보낸_사용자1.getId())
                                .build();
        친구_요청_알림2 = Notification.builder()
                                .receiver(알림이_있는_사용자)
                                .title(REQUEST_FRIEND.getTitle())
                                .content(REQUEST_FRIEND.getContentByFormat(친구_요청을_보낸_사용자2.getName()))
                                .type(REQUEST_FRIEND)
                                .requestId(친구_요청을_보낸_사용자2.getId())
                                .build();

        notificationRepository.saveAll(List.of(친구_요청_알림1, 친구_요청_알림2));
    }
}
