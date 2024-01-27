package com.backend.blooming.notification.application.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum NotificationKey {

    TITLE("title"),
    BODY("body"),
    TYPE("type"),
    REQUEST_ID("requestId");

    private final String value;
}
