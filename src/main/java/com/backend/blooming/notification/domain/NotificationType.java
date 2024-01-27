package com.backend.blooming.notification.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum NotificationType {

    REQUEST_FRIEND("친구 신청", "%s님이 회원님에게 친구 신청을 요청했습니다.");

    private final String title;
    private final String content;

    public String getContentByFormat(final String value) {
        return String.format(content, value);
    }
}
