package com.backend.blooming.notification.application;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.devicetoken.domain.DeviceToken;
import com.backend.blooming.notification.domain.Notification;
import com.backend.blooming.notification.domain.NotificationType;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class FCMNotificationServiceTestFixture {

    @Autowired
    private UserRepository userRepository;

    protected Long 사용자_아이디;
    protected List<DeviceToken> 디바이스_토큰들;
    protected Notification 알림;

    @BeforeEach
    void setUpFixture() {
        final User 사용자 = User.builder()
                             .oAuthId("12345")
                             .oAuthType(OAuthType.KAKAO)
                             .name(new Name("사용자"))
                             .email(new Email("test@email.com"))
                             .color(ThemeColor.BEIGE)
                             .statusMessage("기존 상태 메시지")
                             .build();
        userRepository.save(사용자);
        사용자_아이디 = 사용자.getId();

        final DeviceToken 디바이스_토큰1 = new DeviceToken(사용자.getId(), "token1");
        final DeviceToken 디바이스_토큰2 = new DeviceToken(사용자.getId(), "token2");
        디바이스_토큰들 = List.of(디바이스_토큰1, 디바이스_토큰2);

        알림 = Notification.builder()
                         .receiver(사용자)
                         .title("알림 제목입니다")
                         .content("알림 내용입니다")
                         .type(NotificationType.REQUEST_FRIEND)
                         .requestId(2L)
                         .build();
    }
}
