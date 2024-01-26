package com.backend.blooming.notification.infrastructure.repository;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.notification.domain.Notification;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.backend.blooming.notification.domain.NotificationType.REQUEST_FRIEND;

@SuppressWarnings("NonAsciiCharacters")
public class NotificationRepositoryTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    protected Long 사용자_아이디;
    protected Notification 알림1;
    protected Notification 알림2;

    @BeforeEach
    void setUpFixture() {
        final User 사용자 = User.builder()
                             .oAuthId("12345")
                             .oAuthType(OAuthType.KAKAO)
                             .name(new Name("사용자1"))
                             .email(new Email("user1@email.com"))
                             .build();

        userRepository.saveAll(List.of(사용자));
        사용자_아이디 = 사용자.getId();

        알림1 = Notification.builder()
                          .receiver(사용자)
                          .title("제목1")
                          .content("제목1에 대한 내용입니다.")
                          .type(REQUEST_FRIEND)
                          .requestId(99L)
                          .build();
        알림2 = Notification.builder()
                          .receiver(사용자)
                          .title("제목2")
                          .content("제목2에 대한 내용입니다.")
                          .type(REQUEST_FRIEND)
                          .requestId(100L)
                          .build();

        notificationRepository.saveAll(List.of(알림1, 알림2));
    }
}
