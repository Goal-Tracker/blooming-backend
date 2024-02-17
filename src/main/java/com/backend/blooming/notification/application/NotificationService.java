package com.backend.blooming.notification.application;

import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.notification.application.dto.ReadNotificationsDto;
import com.backend.blooming.notification.application.exception.NotFoundGoalManagerException;
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

import static com.backend.blooming.notification.domain.NotificationType.ACCEPT_FRIEND;
import static com.backend.blooming.notification.domain.NotificationType.POKE;
import static com.backend.blooming.notification.domain.NotificationType.REQUEST_FRIEND;
import static com.backend.blooming.notification.domain.NotificationType.REQUEST_GOAL;

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
        sendNotification(notification);

        return notification.getId();
    }

    private Notification persistNotification(
            final NotificationType notificationType,
            @Nullable final String titleValue,
            final User sender,
            final User receiver
    ) {
        final Notification notification = createNotification(notificationType, titleValue, sender, receiver);

        return notificationRepository.save(notification);
    }

    private static Notification createNotification(
            final NotificationType notificationType,
            @Nullable final String titleValue,
            final User sender,
            final User receiver
    ) {
        return Notification.builder()
                           .receiver(receiver)
                           .title(notificationType.getTitleByFormat(titleValue))
                           .content(notificationType.getContentByFormat(sender.getName()))
                           .type(notificationType)
                           .requestId(sender.getId())
                           .build();
    }

    private void sendNotification(final Notification notification) {
        final User receiver = notification.getReceiver();
        receiver.updateNewAlarm(true);
        fcmNotificationService.sendNotification(notification);
    }

    public Long sendPokeNotification(final Goal goal, final User sender, final User receiver) {
        final Notification notification = persistNotification(POKE, goal.getName(), sender, receiver);
        sendNotification(notification);

        return notification.getId();
    }

    public Long sendAcceptFriendNotification(final Friend friend) {
        final User sender = friend.getRequestedUser();
        final User receiver = friend.getRequestUser();
        final Notification notification = persistNotification(ACCEPT_FRIEND, null, sender, receiver);
        sendNotification(notification);

        return notification.getId();
    }

    public List<Long> sendRequestGoalNotification(final Goal goal) {
        final User sender = getGoalManager(goal);
        final List<User> receivers = getGoalTeams(goal);
        final List<Notification> notifications = persistNotifications(goal, sender, receivers);
        notifications.forEach(this::sendNotification);

        return notifications.stream()
                            .map(Notification::getId)
                            .toList();
    }

    private User getGoalManager(final Goal goal) {
        final List<GoalTeam> teams = goal.getTeams().getGoalTeams();
        final Long managerId = goal.getManagerId();

        return teams.stream()
                    .map(GoalTeam::getUser)
                    .filter(user -> user.getId().equals(managerId))
                    .findFirst()
                    .orElseThrow(NotFoundGoalManagerException::new);
    }

    private List<User> getGoalTeams(final Goal goal) {
        final List<GoalTeam> teams = goal.getTeams().getGoalTeams();
        final Long managerId = goal.getManagerId();

        return teams.stream()
                    .map(GoalTeam::getUser)
                    .filter(user -> !user.getId().equals(managerId))
                    .toList();
    }

    private List<Notification> persistNotifications(final Goal goal, final User sender, final List<User> receivers) {
        final List<Notification> notifications = receivers.stream()
                                                          .map(receiver -> createNotification(REQUEST_GOAL, goal.getName(), sender, receiver))
                                                          .toList();

        return notificationRepository.saveAll(notifications);
    }
}
