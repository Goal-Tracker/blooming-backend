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
    NULL_OREMPTY_NAME("이름은 비어있을 수 없습니다."),
    LONGER_THAN_MAXIMUM_NAME("이름의 최대 길이를 초과했습니다."),

    // 테마 색상
    UNSUPPORTED_THEME_COLOR("지원하지 않는 테마 색상입니다."),

    // 친구
    ALREADY_REQUESTED_FRIEND("이미 친구를 요청한 사용자입니다."),
    NOT_FOUND_FRIEND_REQUEST("해당 친구 요청을 조회할 수 없습니다."),
    FRIEND_ACCEPTANCE_FORBIDDEN("친구 요청을 수락할 권한이 없습니다."),
    DELETE_FRIEND_FORBIDDEN("친구를 삭제할 권한이 없습니다.");

    private final String message;
}
