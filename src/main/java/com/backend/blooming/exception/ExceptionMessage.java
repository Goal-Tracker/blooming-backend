package com.backend.blooming.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ExceptionMessage {

    // 인증 & 인가
    INVALID_AUTHORIZATION_TOKEN("유효하지 않은 토큰입니다."),
    KAKAO_SERVER_UNAVAILABLE("카카오 서버에 문제가 발생했습니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    WRONG_TOKEN_TYPE("Bearer 타입의 토큰이 아닙니다."),
    EXPIRED_TOKEN("기한이 만료된 토큰입니다."),
    UNSUPPORTED_OAUTH_TYPE("지원하지 않는 소셜 로그인 방식입니다."),

    // 사용자 정보
    NOT_FOUND_USER("사용자를 조회할 수 없습니다."),

    // 테마 색상
    UNSUPPORTED_THEME_COLOR("지원하지 않는 테마 색상입니다."),

    // 날짜 파싱
    DATE_FORMAT_PARSE_FAILED("날짜 파싱에 실패했습니다."),

    // 골 추가
    GOAL_NOT_FOUND("골 정보를 찾을 수 없습니다."),
    GOAL_TEAM_NOT_FOUND("골 팀 정보를 찾을 수 없습니다."),
    INVALID_GOAL_START_DAY("시작 날짜가 현재 날짜 이전입니다."),
    INVALID_GOAL_END_DAY("종료 날짜가 현재 날짜 이전입니다."),
    INVALID_GOAL_PERIOD("시작 날짜가 종료 날짜 이후입니다."),
    INVALID_GOAL_DAYS("골 날짜 수가 범위 밖입니다.(범위: 1~100)"),
    INVALID_IN_PROGRESS_DAYS("현재 진행중인 날짜 수가 범위 밖입니다.(범위: 1~전체 날짜 수)");

    private final String message;
}
