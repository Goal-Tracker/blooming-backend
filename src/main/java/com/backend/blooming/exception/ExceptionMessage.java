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
    ;

    private final String message;
}
