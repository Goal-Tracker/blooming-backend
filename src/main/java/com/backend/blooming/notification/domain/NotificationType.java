package com.backend.blooming.notification.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum NotificationType {

    REQUEST_FRIEND("친구 신청", "%s님이 회원님에게 친구 신청을 요청했습니다."),
    ACCEPT_FRIEND("친구 수락", "%s님이 회원님의 친구 신청을 수락했습니다."),
    POKE("콕 찌르기 - %s", "%s님이 회원님을 콕 찔렀습니다.");

    private final String title;
    private final String content;

    public String getTitleByFormat(@Nullable final String value) {
        return String.format(title, value);
    }

    public String getContentByFormat(final String value) {
        return String.format(content, value);
    }
}
