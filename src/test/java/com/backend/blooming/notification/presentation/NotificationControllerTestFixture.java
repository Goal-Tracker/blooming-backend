package com.backend.blooming.notification.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.notification.application.dto.ReadNotificationsDto;
import com.backend.blooming.notification.domain.NotificationType;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class NotificationControllerTestFixture {

    protected TokenType 액세스_토큰_타입 = TokenType.ACCESS;
    protected String 액세스_토큰 = "Bearer access_token";
    protected Long 사용자_아이디 = 1L;
    protected AuthClaims 사용자_토큰_정보 = new AuthClaims(사용자_아이디);

    protected ReadNotificationsDto.ReadNotificationDto 알림_정보_dto1 =
            new ReadNotificationsDto.ReadNotificationDto(
                    1L,
                    "알림 1",
                    "알림 1에 대한 내용입니다.",
                    NotificationType.REQUEST_FRIEND,
                    2L
            );
    protected ReadNotificationsDto.ReadNotificationDto 알림_정보_dto2 =
            new ReadNotificationsDto.ReadNotificationDto(
                    2L,
                    "알림 2",
                    "알림 2에 대한 내용입니다.",
                    NotificationType.REQUEST_FRIEND,
                    3L
            );
    protected ReadNotificationsDto 알림_목록_정보_dto = new ReadNotificationsDto(List.of(알림_정보_dto1, 알림_정보_dto2));
}
