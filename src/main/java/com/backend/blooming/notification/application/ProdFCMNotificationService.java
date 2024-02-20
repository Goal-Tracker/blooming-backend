package com.backend.blooming.notification.application;

import com.backend.blooming.devicetoken.domain.DeviceToken;
import com.backend.blooming.devicetoken.infrastructure.repository.DeviceTokenRepository;
import com.backend.blooming.notification.domain.Notification;
import com.backend.blooming.user.domain.User;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.backend.blooming.notification.application.util.NotificationKey.REQUEST_ID;
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
        if (deviceTokens.isEmpty()) {
            return;
        }

        final List<Message> messages = createMessages(notification, deviceTokens);

        try {
            final BatchResponse batchResponse = firebaseMessaging.sendAll(messages);
            checkAllSuccess(batchResponse);
        } catch (FirebaseMessagingException exception) {
            log.warn("보낼 알림이 없거나, 알림 보내기에 실패했습니다. : ", exception);
        }
    }

    private List<String> getDeviceTokens(final User receiver) {
        return deviceTokenRepository.findAllByUserIdAndActiveIsTrue(receiver.getId())
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
                      .setAndroidConfig(createAndroidConfig(notification))
                      .putData(TYPE.getValue(), notification.getType().name())
                      .putData(REQUEST_ID.getValue(), notification.getRequestId().toString())
                      .build();
    }

    private AndroidConfig createAndroidConfig(final Notification notification) {
        return AndroidConfig.builder()
                            .setNotification(createAndroidNotification(notification))
                            .build();
    }

    private AndroidNotification createAndroidNotification(final Notification notification) {
        return AndroidNotification.builder()
                                  .setTitle(notification.getTitle())
                                  .setBody(notification.getContent())
                                  .build();
    }

    private void checkAllSuccess(final BatchResponse batchResponse) {
        if (batchResponse.getFailureCount() > 0) {
            final List<String> failResponses = batchResponse.getResponses()
                                                            .stream()
                                                            .filter(sendResponse -> !sendResponse.isSuccessful())
                                                            .map(sendResponse ->
                                                                    sendResponse.getException()
                                                                                .getMessage()
                                                            )
                                                            .toList();

            log.warn("알림 보내기에 실패 요청이 있습니다. 실패 개수: {}, {}", batchResponse.getFailureCount(), failResponses);
        }
    }
}
