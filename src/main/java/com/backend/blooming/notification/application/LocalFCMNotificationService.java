package com.backend.blooming.notification.application;

import com.backend.blooming.notification.domain.Notification;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local | test")
public class LocalFCMNotificationService implements FCMNotificationService {

    @Override
    public void sendNotification(final Notification notification) {}
}
