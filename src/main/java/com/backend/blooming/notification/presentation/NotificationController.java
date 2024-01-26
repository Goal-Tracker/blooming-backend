package com.backend.blooming.notification.presentation;

import com.backend.blooming.authentication.presentation.anotaion.Authenticated;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedUser;
import com.backend.blooming.notification.application.NotificationService;
import com.backend.blooming.notification.application.dto.ReadNotificationsDto;
import com.backend.blooming.notification.presentation.dto.ReadNotificationsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(headers = "X-API-VERSION=1")
    public ReadNotificationsResponse readAllByUserId(@Authenticated final AuthenticatedUser authenticatedUser) {
        final ReadNotificationsDto notificationsDto = notificationService.readAllByUserId(authenticatedUser.userId());

        return ReadNotificationsResponse.from(notificationsDto);
    }
}
