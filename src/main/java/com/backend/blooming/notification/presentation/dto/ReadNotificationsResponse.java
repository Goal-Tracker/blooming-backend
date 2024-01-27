package com.backend.blooming.notification.presentation.dto;

import com.backend.blooming.notification.application.dto.ReadNotificationsDto;

import java.util.List;

public record ReadNotificationsResponse(List<ReadNotificationResponse> notifications) {

    public static ReadNotificationsResponse from(final ReadNotificationsDto notificationsDto) {
        final List<ReadNotificationResponse> notificationResponses = notificationsDto.notifications()
                                                                                     .stream()
                                                                                     .map(ReadNotificationResponse::from)
                                                                                     .toList();

        return new ReadNotificationsResponse(notificationResponses);
    }

    public record ReadNotificationResponse(Long id, String title, String content, String type, Long requestId) {

        public static ReadNotificationResponse from(
                final ReadNotificationsDto.ReadNotificationDto readNotificationDto
        ) {
            return new ReadNotificationResponse(
                    readNotificationDto.id(),
                    readNotificationDto.title(),
                    readNotificationDto.content(),
                    readNotificationDto.type().name(),
                    readNotificationDto.requestId()
            );
        }
    }
}
