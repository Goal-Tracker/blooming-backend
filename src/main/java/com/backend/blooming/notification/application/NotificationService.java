package com.backend.blooming.notification.application;

import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.notification.application.dto.ReadNotificationsDto;
import com.backend.blooming.notification.domain.Notification;
import com.backend.blooming.notification.domain.NotificationType;
import com.backend.blooming.notification.infrastructure.repository.NotificationRepository;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.backend.blooming.notification.domain.NotificationType.POKE;
import static com.backend.blooming.notification.domain.NotificationType.REQUEST_FRIEND;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final FCMNotificationService fcmNotificationService;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public ReadNotificationsDto readAllByUserId(final Long userId) {
        final User user = userRepository.findByIdAndDeletedIsFalse(userId)
                                        .orElseThrow(NotFoundUserException::new);
        final List<Notification> notifications = notificationRepository.findAllByReceiverId(userId);

        user.updateNewAlarm(false);

        return ReadNotificationsDto.from(notifications);
    }

    public Long sendRequestFriendNotification(final Friend friend) {
        final User sender = friend.getRequestUser();
        final User receiver = friend.getRequestedUser();
        final Notification notification = persistNotification(REQUEST_FRIEND, null, sender, receiver);
        sendNotification(receiver, notification);

        return notification.getId();
    }

    private Notification persistNotification(
            final NotificationType notificationType,
            @Nullable final String titleValue,
            final User sender,
            final User receiver
    ) {
        final Notification notification = Notification.builder()
                                                      .receiver(receiver)
                                                      .title(notificationType.getTitleByFormat(titleValue))
                                                      .content(notificationType.getContentByFormat(sender.getName()))
                                                      .type(notificationType)
                                                      .requestId(sender.getId())
                                                      .build();

        return notificationRepository.save(notification);
    }

    private void sendNotification(final User receiver, final Notification notification) {
        receiver.updateNewAlarm(true);
        fcmNotificationService.sendNotification(notification);
    }

    public Long sendPokeNotification(final Goal goal, final User sender, final User receiver) {
        final Notification notification = persistNotification(POKE, goal.getName(), sender, receiver);
        sendNotification(receiver, notification);

        return notification.getId();
    }
}
