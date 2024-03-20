package com.backend.blooming.notification.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.notification.application.dto.ReadNotificationsDto;
import com.backend.blooming.notification.application.exception.NotFoundGoalManagerException;
import com.backend.blooming.notification.domain.Notification;
import com.backend.blooming.notification.infrastructure.repository.NotificationRepository;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.backend.blooming.notification.domain.NotificationType.ACCEPT_FRIEND;
import static com.backend.blooming.notification.domain.NotificationType.POKE;
import static com.backend.blooming.notification.domain.NotificationType.REQUEST_FRIEND;
import static com.backend.blooming.notification.domain.NotificationType.REQUEST_GOAL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

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
    void 특정_사용자의_전체_알림_목록을_조회한다() {
        // when
        final ReadNotificationsDto actual = notificationService.readAllByUserId(알림이_있는_사용자.getId());

        // then
        final User user = userRepository.findById(알림이_있는_사용자.getId()).get();
        assertSoftly(softAssertions -> {
            final List<ReadNotificationsDto.ReadNotificationDto> notifications = actual.notifications();
            softAssertions.assertThat(notifications).hasSize(2);
            softAssertions.assertThat(notifications.get(0).id()).isEqualTo(친구_요청_알림1.getId());
            softAssertions.assertThat(notifications.get(0).title()).isEqualTo(친구_요청_알림1.getTitle());
            softAssertions.assertThat(notifications.get(1).id()).isEqualTo(친구_요청_알림2.getId());
            softAssertions.assertThat(notifications.get(1).title()).isEqualTo(친구_요청_알림2.getTitle());
            softAssertions.assertThat(user.isNewAlarm()).isFalse();
        });
    }

    @Test
    void 특정_사용자의_전체_알림_목록을_조회시_존재하지_않는_사용자라면_예외를_반환한다() {
        assertThatThrownBy(() -> notificationService.readAllByUserId(존재하지_않는_사용자_아이디))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 친구_요청에_대한_알림을_저장한다() {
        // when
        final Long actual = notificationService.sendRequestFriendNotification(친구_요청에_대한_친구);

        // then
        final Notification notification = notificationRepository.findById(actual).get();
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPositive();
            softAssertions.assertThat(notification.getReceiver().getId()).isEqualTo(친구_요청을_받은_사용자.getId());
            softAssertions.assertThat(notification.getTitle()).isEqualTo(REQUEST_FRIEND.getTitleByFormat(null));
            softAssertions.assertThat(notification.getContent()).contains(친구_요청을_보낸_사용자.getName());
            softAssertions.assertThat(notification.getType()).isEqualTo(REQUEST_FRIEND);
            softAssertions.assertThat(notification.getRequestId()).isEqualTo(친구_요청을_보낸_사용자.getId());
            softAssertions.assertThat(친구_요청을_받은_사용자.isNewAlarm()).isTrue();
        });
    }

    @Test
    void 친구_수락에_대한_알림을_저장한다() {
        // when
        final Long actual = notificationService.sendAcceptFriendNotification(친구_수락에_대한_친구);

        // then
        final Notification notification = notificationRepository.findById(actual).get();
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPositive();
            softAssertions.assertThat(notification.getReceiver().getId()).isEqualTo(친구_요청을_보낸_사용자.getId());
            softAssertions.assertThat(notification.getTitle()).isEqualTo(ACCEPT_FRIEND.getTitleByFormat(null));
            softAssertions.assertThat(notification.getContent()).contains(친구_요청을_수락한_사용자.getName());
            softAssertions.assertThat(notification.getType()).isEqualTo(ACCEPT_FRIEND);
            softAssertions.assertThat(notification.getRequestId()).isNull();
            softAssertions.assertThat(친구_요청을_보낸_사용자.isNewAlarm()).isTrue();
        });
    }

    @Test
    void 콕_찌르기_요청에_대한_알림을_저장한다() {
        // when
        final Long actual = notificationService.sendPokeNotification(골, 콕_찌르기_요청자, 콕_찌르기_수신자);

        // then
        final Notification notification = notificationRepository.findById(actual).get();
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPositive();
            softAssertions.assertThat(notification.getReceiver().getId()).isEqualTo(콕_찌르기_수신자.getId());
            softAssertions.assertThat(notification.getTitle()).isEqualTo(POKE.getTitleByFormat(골.getName()));
            softAssertions.assertThat(notification.getContent()).contains(콕_찌르기_요청자.getName());
            softAssertions.assertThat(notification.getType()).isEqualTo(POKE);
            softAssertions.assertThat(notification.getRequestId()).isNull();
            softAssertions.assertThat(콕_찌르기_수신자.isNewAlarm()).isTrue();
        });
    }

    @Test
    void 골_초대_요청에_대한_알림을_저장한다() {
        // when
        final List<Long> actuals = notificationService.sendRequestGoalNotification(골, 골.getTeams());

        // then
        actuals.forEach(actual -> {
            final Notification notification = notificationRepository.findById(actual).get();
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).isPositive();
                softAssertions.assertThat(notification.getReceiver().getId())
                              .isIn(골_요청을_받은_사용자1.getId(), 골_요청을_받은_사용자2.getId());
                softAssertions.assertThat(notification.getTitle())
                              .isEqualTo(REQUEST_GOAL.getTitleByFormat(골.getName()));
                softAssertions.assertThat(notification.getContent()).contains(골_관리자.getName());
                softAssertions.assertThat(notification.getType()).isEqualTo(REQUEST_GOAL);
                softAssertions.assertThat(notification.getRequestId()).isNull();
                softAssertions.assertThat(골_요청을_받은_사용자1.isNewAlarm()).isTrue();
                softAssertions.assertThat(골_요청을_받은_사용자2.isNewAlarm()).isTrue();
            });
        });
    }

    @Test
    void 골_초대_요청시_팀원에_골_관리자가_없다면_예외를_발생시킨다() {
        // when & then
        assertThatThrownBy(() -> notificationService.sendRequestGoalNotification(
                팀원에_관리자가_없는_골,
                팀원에_관리자가_없는_골.getTeams()
        )).isInstanceOf(NotFoundGoalManagerException.class);
    }
}
