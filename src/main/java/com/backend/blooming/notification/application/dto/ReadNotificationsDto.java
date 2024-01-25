package com.backend.blooming.notification.application.dto;

import com.backend.blooming.notification.domain.Notification;
import com.backend.blooming.notification.domain.NotificationType;

import java.util.List;

public record ReadNotificationsDto(List<ReadNotificationDto> notifications) {


    public static ReadNotificationsDto from(final List<Notification> notifications) {
        final List<ReadNotificationDto> notificationDtos = notifications.stream()
                                                                          .map(ReadNotificationDto::from)
                                                                          .toList();

        return new ReadNotificationsDto(notificationDtos);
    }

    public record ReadNotificationDto(
            Long id,
            String title,
            String content,
            NotificationType type,
            Long requestId
    ) {

        public static ReadNotificationDto from(final Notification notification) {
            return new ReadNotificationDto(
                    notification.getId(),
                    notification.getTitle(),
                    notification.getContent(),
                    notification.getType(),
                    notification.getRequestId()
            );
        }
    }
}
