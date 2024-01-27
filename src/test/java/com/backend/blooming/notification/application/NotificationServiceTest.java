package com.backend.blooming.notification.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.notification.application.dto.ReadNotificationsDto;
import com.backend.blooming.notification.domain.Notification;
import com.backend.blooming.notification.domain.NotificationType;
import com.backend.blooming.notification.infrastructure.repository.NotificationRepository;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NotificationServiceTest extends NotificationServiceTestFixture {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 친구_요청에_대한_알림을_저장한다() {
        // when
        final Long actual = notificationService.sendRequestFriendNotification(보낸_친구_요청);

        // then
        final Notification notification = notificationRepository.findById(actual).get();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPositive();
            softAssertions.assertThat(notification.getReceiver().getId()).isEqualTo(친구_요청을_받은_사용자.getId());
            softAssertions.assertThat(notification.getTitle()).isEqualTo(NotificationType.REQUEST_FRIEND.getTitle());
            softAssertions.assertThat(notification.getContent()).contains(친구_요청을_보낸_사용자.getName());
            softAssertions.assertThat(notification.getType()).isEqualTo(NotificationType.REQUEST_FRIEND);
            softAssertions.assertThat(notification.getRequestId()).isEqualTo(친구_요청을_보낸_사용자.getId());
            softAssertions.assertThat(친구_요청을_받은_사용자.isNewAlarm()).isTrue();
        });
    }

    @Test
    void 특정_사용자의_전체_알림_목록을_조회한다() {
        // when
        final ReadNotificationsDto actual = notificationService.readAllByUserId(알림이_있는_사용자.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            final List<ReadNotificationsDto.ReadNotificationDto> notifications = actual.notifications();
            softAssertions.assertThat(notifications).hasSize(2);
            softAssertions.assertThat(notifications.get(0).id()).isEqualTo(친구_요청_알림1.getId());
            softAssertions.assertThat(notifications.get(0).title()).isEqualTo(친구_요청_알림1.getTitle());
            softAssertions.assertThat(notifications.get(1).id()).isEqualTo(친구_요청_알림2.getId());
            softAssertions.assertThat(notifications.get(1).title()).isEqualTo(친구_요청_알림2.getTitle());
            softAssertions.assertThat(알림이_있는_사용자.isNewAlarm()).isFalse();
        });
    }

    @Test
    void 특정_사용자의_전체_알림_목록을_조회시_존재하지_않는_사용자라면_예외를_반환한다() {
        assertThatThrownBy(() -> notificationService.readAllByUserId(존재하지_않는_사용자_아이디))
                .isInstanceOf(NotFoundUserException.class);
    }
}
