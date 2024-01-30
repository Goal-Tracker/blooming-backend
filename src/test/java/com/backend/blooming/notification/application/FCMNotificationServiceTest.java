package com.backend.blooming.notification.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.devicetoken.infrastructure.repository.DeviceTokenRepository;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.SendResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FCMNotificationServiceTest extends FCMNotificationServiceTestFixture {

    @InjectMocks
    private ProdFCMNotificationService fcmNotificationService;

    @Mock
    private DeviceTokenRepository deviceTokenRepository;

    @Mock
    private FirebaseMessaging firebaseMessaging;

    @Mock
    private BatchResponse batchResponse;

    @Mock
    private SendResponse sendResponse;

    @Test
    void 사용자에게_알림을_보낼때_해당_사용자의_디바이스_토큰이_없다면_아무것도_수행되지_않는다() {
        // given
        given(deviceTokenRepository.findAllByUserIdAndActiveIsTrue(사용자_아이디)).willReturn(List.of());

        // when
        fcmNotificationService.sendNotification(알림);

        // then
        verify(sendResponse, never()).isSuccessful();
        verify(batchResponse, never()).getFailureCount();
    }

    @Test
    void 사용자에게_알림을_보낼때_성공적이라면_알림_전송에_대한_성공_여부를_호출하지_않는다() throws FirebaseMessagingException {
        // given
        given(deviceTokenRepository.findAllByUserIdAndActiveIsTrue(사용자_아이디)).willReturn(디바이스_토큰들);
        given(batchResponse.getFailureCount()).willReturn(0);
        given(firebaseMessaging.sendAll(anyList())).willReturn(batchResponse);

        // when
        fcmNotificationService.sendNotification(알림);

        // then
        verify(sendResponse, never()).isSuccessful();
    }

    @Test
    void 사용자에게_알림을_보낼때_알림요청이_실패한_것이_하나라도_있다면_각_알림_전송에_대한_성공_여부를_한번이상_호출한다() throws FirebaseMessagingException {
        // given
        given(deviceTokenRepository.findAllByUserIdAndActiveIsTrue(사용자_아이디)).willReturn(디바이스_토큰들);
        given(batchResponse.getFailureCount()).willReturn(1);
        given(sendResponse.isSuccessful()).willReturn(true, false);
        given(batchResponse.getResponses()).willReturn(List.of(sendResponse));
        given(firebaseMessaging.sendAll(anyList())).willReturn(batchResponse);

        // when
        fcmNotificationService.sendNotification(알림);

        // then
        verify(sendResponse, atLeastOnce()).isSuccessful();
        verify(batchResponse, times(2)).getFailureCount();
    }
}
