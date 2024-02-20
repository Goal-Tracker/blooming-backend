package com.backend.blooming.notification.application;

import com.backend.blooming.devicetoken.domain.DeviceToken;
import com.backend.blooming.devicetoken.infrastructure.repository.DeviceTokenRepository;
import com.backend.blooming.notification.domain.Notification;
import com.backend.blooming.user.domain.User;
import com.google.firebase.messaging.AndroidConfig;
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
        if (deviceTokens.isEmpty()) {
            log.debug("사용자 {} - device token이 없습니다.", notification.getReceiver().getName());
            return;
        }

        final List<Message> messages = createMessages(notification, deviceTokens);
        log.debug("사용자 {} - 보낼 메시지: {}.", notification.getReceiver().getName(), messages.toString());

        try {
            final BatchResponse batchResponse = firebaseMessaging.sendAll(messages);
            checkAllSuccess(batchResponse);
            log.debug("사용자 {} - 알림 보내기 성공.", notification.getReceiver().getName());
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
        final AndroidConfig androidConfig = createAndroidConfig();

        return deviceTokens.stream()
                           .map(deviceToken -> createMessage(notification, deviceToken, androidConfig))
                           .toList();
    }

    private static AndroidConfig createAndroidConfig() {
        return AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build();
    }

    private Message createMessage(
            final Notification notification,
            final String deviceToken,
            final AndroidConfig androidConfig
    ) {
        return Message.builder()
                      .setAndroidConfig(androidConfig)
                      .setToken(deviceToken)
                      .putData(TITLE.getValue(), notification.getTitle())
                      .putData(BODY.getValue(), notification.getContent())
                      .putData(TYPE.getValue(), notification.getType().name())
                      .putData(REQUEST_ID.getValue(), notification.getRequestId().toString())
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
