package com.backend.blooming.notification.application;

import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.notification.domain.Notification;
import com.backend.blooming.notification.infrastructure.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.backend.blooming.notification.domain.NotificationType.REQUEST_FRIEND;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Long sendRequestFriendNotification(final Friend friend) {
        final Notification notification = Notification.builder()
                                                      .receiver(friend.getRequestedUser())
                                                      .title(REQUEST_FRIEND.getTitle())
                                                      .content(REQUEST_FRIEND.getContentByFormat(
                                                              friend.getRequestUser().getName()
                                                      ))
                                                      .type(REQUEST_FRIEND)
                                                      .requestId(friend.getRequestedUser().getId())
                                                      .build();

        return notificationRepository.save(notification)
                                     .getId();
    }
}
