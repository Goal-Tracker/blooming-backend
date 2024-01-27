package com.backend.blooming.notification.application;

import com.backend.blooming.notification.domain.Notification;

public interface FCMNotificationService {

    void sendNotification(final Notification notification);
}
