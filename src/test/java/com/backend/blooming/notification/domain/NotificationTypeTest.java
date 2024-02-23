package com.backend.blooming.notification.domain;

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
class NotificationTypeTest {

    @ParameterizedTest(name = "{0} 알림 타입의 제목")
    @MethodSource("NotificationTypeAndTitleValueAndExpectedProvider")
    void 알림_요청의_제목을_원하는_값을_포함해_반환한다(
            final NotificationType notificationType,
            final String titleValue,
            final String expected
    ) {
        // when
        final String actual = notificationType.getTitleByFormat(titleValue);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> NotificationTypeAndTitleValueAndExpectedProvider() {
        final String titleValue = "제목";
        return Stream.of(
                arguments(NotificationType.REQUEST_FRIEND, titleValue, "친구 신청"),
                arguments(NotificationType.POKE, titleValue, "콕 찌르기 - " + titleValue)
        );
    }

    @ParameterizedTest(name = "{0} 알림 타입의 내용")
    @MethodSource("NotificationTypeAndContentValueAndExpectedProvider")
    void 알림_요청의_내용을_원하는_값을_포함해_반환한다(
            final NotificationType notificationType,
            final String contentValue,
            final String expected
    ) {
        // when
        final String actual = notificationType.getContentByFormat(contentValue);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> NotificationTypeAndContentValueAndExpectedProvider() {
        final String contentValue = "사용자";
        return Stream.of(
                arguments(NotificationType.REQUEST_FRIEND, contentValue, contentValue + "님이 회원님에게 친구 신청을 요청했습니다."),
                arguments(NotificationType.POKE, contentValue, contentValue + "님이 회원님을 콕 찔렀습니다.")
        );
    }
}
