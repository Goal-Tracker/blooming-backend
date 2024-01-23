package com.backend.blooming.notification.application;

import com.backend.blooming.configuration.IsolateDatabase;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NotificationServiceTestTest extends NotificationServiceTestFixture {

    @Autowired
    private NotificationService notificationService;

    @Test
    void 친구_요청에_대한_알림을_저장한다() {
        // when
        final Long actual = notificationService.sendRequestFriendNotification(보낸_친구_요청);

        // then
        assertThat(actual).isPositive();
    }
}
