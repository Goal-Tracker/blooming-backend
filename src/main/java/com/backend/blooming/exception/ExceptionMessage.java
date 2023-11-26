package com.backend.blooming.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ExceptionMessage {

    // 날짜 파싱
    DATE_FORMAT_PARSE_FAILED("날짜 파싱에 실패했습니다."),

    // 골 추가
    GOAL_NOT_FOUND("골 정보를 찾을 수 없습니다."),
    INVALID_GOAL_START_DAY("시작 날짜가 현재 날짜 이전입니다."),
    INVALID_GOAL_END_DAY("종료 날짜가 현재 날짜 이전입니다."),
    INVALID_GOAL_PERIOD("시작 날짜가 종료 날짜 이후입니다."),
    INVALID_GOAL_DAYS("골 날짜수가 1 이상이 아닙니다.");

    private final String message;
}
