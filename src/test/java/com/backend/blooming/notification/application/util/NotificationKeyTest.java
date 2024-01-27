package com.backend.blooming.notification.application.util;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NotificationKeyTest {

    @ParameterizedTest
    @MethodSource("provideNotificationKey")
    void 알림_요청_키_이름을_반환한다(final NotificationKey notificationKey, final String expect) {
        // when
        final String actual = notificationKey.getValue();

        // then
        assertThat(actual).isEqualTo(expect);
    }

    static Stream<Arguments> provideNotificationKey() {
        return Stream.of(
                arguments(NotificationKey.TITLE, "title"),
                arguments(NotificationKey.BODY, "body"),
                arguments(NotificationKey.TYPE, "type"),
                arguments(NotificationKey.REQUEST_ID, "requestId")
        );
    }
}
