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

    // 사용자
    NOT_FOUND_USER("사용자를 조회할 수 없습니다."),
    NULL_OR_EMPTY_EMAIL("이메일은 비어있을 수 없습니다."),
    LONGER_THAN_MAXIMUM_EMAIL("이메일의 최대 길이를 초과했습니다."),
    INVALID_EMAIL_FORMAT("이메일 형식에 어긋났습니다."),
    NULL_OR_EMPTY_NAME("이름은 비어있을 수 없습니다."),
    LONGER_THAN_MAXIMUM_NAME("이름의 최대 길이를 초과했습니다."),

    // 테마 색상
    UNSUPPORTED_THEME_COLOR("지원하지 않는 테마 색상입니다."),

    // 디바이스 토큰
    NOT_FOUND_DEVICE_TOKEN("디바이스 토큰을 찾을 수 없습니다."),

    // 친구
    ALREADY_REQUESTED_FRIEND("이미 친구를 요청한 사용자입니다."),
    NOT_FOUND_FRIEND_REQUEST("해당 친구 요청을 조회할 수 없습니다."),
    FRIEND_ACCEPTANCE_FORBIDDEN("친구 요청을 수락할 권한이 없습니다."),
    DELETE_FRIEND_FORBIDDEN("친구를 삭제할 권한이 없습니다."),

    // 골 추가
    GOAL_NOT_FOUND("골 정보를 찾을 수 없습니다."),
    GOAL_TEAM_NOT_FOUND("골 팀 정보를 찾을 수 없습니다."),
    INVALID_GOAL_START_DAY("시작 날짜가 현재 날짜 이전입니다."),
    INVALID_GOAL_END_DAY("종료 날짜가 현재 날짜 이전입니다."),
    INVALID_GOAL_PERIOD("시작 날짜가 종료 날짜 이후입니다."),
    INVALID_GOAL_DAYS("골 날짜 수가 범위 밖입니다.(범위: 1~100)"),
    INVALID_USERS_SIZE("골에 참여하는 친구가 5명 초과입니다."),
    INVALID_USER_TO_PARTICIPATE("골에 참여할 수 없는 사용자입니다. 골에는 친구인 사용자만 초대할 수 있습니다."),
    DELETE_GOAL_FORBIDDEN("골을 삭제할 권한이 없습니다."),
    UPDATE_GOAL_FORBIDDEN("골을 수정할 권한이 없습니다."),
    UPDATE_END_DATE_FORBIDDEN("종료날짜는 기존 날짜보다 이전으로 수정 될 수 없습니다."),
    UPDATE_TEAMS_FORBIDDEN("기존 사용자는 수정할 수 없습니다.");

    private final String message;
}
