package com.backend.blooming.notification.application;

import com.backend.blooming.devicetoken.domain.DeviceToken;
import com.backend.blooming.devicetoken.infrastructure.repository.DeviceTokenRepository;
import com.backend.blooming.notification.domain.Notification;
import com.backend.blooming.user.domain.User;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.backend.blooming.notification.application.util.NotificationKey.BODY;
import static com.backend.blooming.notification.application.util.NotificationKey.REQUEST_ID;
import static com.backend.blooming.notification.application.util.NotificationKey.TITLE;
import static com.backend.blooming.notification.application.util.NotificationKey.TYPE;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@Profile("prod | dev")
public class ProdFCMNotificationService implements FCMNotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final DeviceTokenRepository deviceTokenRepository;

    public void sendNotification(final Notification notification) {
        final List<String> deviceTokens = getDeviceTokens(notification.getReceiver());
        final List<Message> messages = createMessages(notification, deviceTokens);

        try {
            final BatchResponse batchResponse = firebaseMessaging.sendAll(messages);
            checkAllSuccess(batchResponse);
        } catch (FirebaseMessagingException exception) {
            log.error("보낼 알림이 없거나, 알림 보내기에 실패했습니다. : ", exception);
        }
    }

    private List<String> getDeviceTokens(final User receiver) {
        return deviceTokenRepository.readAllByUserIdAndDeletedIsFalse(receiver.getId())
                                    .stream()
                                    .map(DeviceToken::getToken)
                                    .toList();
    }

    private List<Message> createMessages(final Notification notification, final List<String> deviceTokens) {
        return deviceTokens.stream()
                           .map(deviceToken -> createMessage(notification, deviceToken))
                           .toList();
    }

    private Message createMessage(final Notification notification, final String deviceToken) {
        return Message.builder()
                      .setToken(deviceToken)
                      .putData(TITLE.getValue(), notification.getTitle())
                      .putData(BODY.getValue(), notification.getContent())
                      .putData(TYPE.getValue(), notification.getType().name())
                      .putData(REQUEST_ID.getValue(), notification.getRequestId().toString())
                      .build();
    }

    private void checkAllSuccess(final BatchResponse batchResponse) {
        if (batchResponse.getFailureCount() > 0) {
            final List<SendResponse> failResponses = batchResponse.getResponses()
                                                                  .stream()
                                                                  .filter(sendResponse -> !sendResponse.isSuccessful())
                                                                  .toList();

            log.warn("알림 보내기에 실패 요청이 있습니다. 실패 개수: {}, {}", batchResponse.getFailureCount(), failResponses);
        }
    }
}
