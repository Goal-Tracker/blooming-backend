package com.backend.blooming.notification.infrastructure.repository;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.notification.domain.Notification;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NotificationRepositoryTest extends NotificationRepositoryTestFixture {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void 특정_사용자_아이디의_전체_알림_목록을_조회한다() {
        // when
        final List<Notification> actual = notificationRepository.findAllByReceiverId(사용자_아이디);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).getId()).isPositive();
            softAssertions.assertThat(actual.get(0).getReceiver().getId()).isEqualTo(사용자_아이디);
            softAssertions.assertThat(actual.get(0).getTitle()).isEqualTo(알림1.getTitle());
            softAssertions.assertThat(actual.get(1).getId()).isPositive();
            softAssertions.assertThat(actual.get(1).getReceiver().getId()).isEqualTo(사용자_아이디);
            softAssertions.assertThat(actual.get(1).getTitle()).isEqualTo(알림2.getTitle());
        });
    }
}
