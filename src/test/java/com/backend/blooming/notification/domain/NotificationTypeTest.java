package com.backend.blooming.notification.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NotificationTypeTest {

    @Test
    void 알림_요청의_내용을_원하는_값을_포함해_반환한다() {
        // given
        final String name = "사용자";

        // when
        final String actual = NotificationType.REQUEST_FRIEND.getContentByFormat(name);

        // then
        assertThat(actual).isEqualTo("사용자님이 회원님에게 친구 신청을 요청했습니다.");
    }
}
