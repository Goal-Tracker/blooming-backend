package com.backend.blooming.report.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Category {

    USER("사용자 신고"),
    GOAL("골 신고"),
    STAMP("스탬프 신고");

    private final String content;
}
