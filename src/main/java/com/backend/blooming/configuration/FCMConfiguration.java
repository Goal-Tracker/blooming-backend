package com.backend.blooming.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
@Profile("prod | dev")
public class FCMConfiguration {

    @Value("${fcm.key.path}")
    private String fcmPrivateKeyPath;

    @Value("${fcm.key.scope}")
    private String fireBaseScope;

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        final FirebaseApp firebaseApp = getFirebaseApp().orElse(FirebaseApp.initializeApp(createFirebaseOptions()));
        log.debug("firebase app: {}, firebaseMessage instance: {}", firebaseApp, FirebaseMessaging.getInstance(firebaseApp));

        return FirebaseMessaging.getInstance(firebaseApp);
    }

    private Optional<FirebaseApp> getFirebaseApp() {
        final List<FirebaseApp> firebaseApps = FirebaseApp.getApps();

        if (firebaseApps == null || firebaseApps.isEmpty()) {
            log.debug("firebae app이 없음");
            return Optional.empty();
        }

        log.debug("firebae app이 있어 찾아옴");
        return firebaseApps.stream()
                           .filter(firebaseApp -> firebaseApp.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
                           .findAny();
    }

    private FirebaseOptions createFirebaseOptions() throws IOException {
        return new FirebaseOptions.Builder()
                .setCredentials(createGoogleCredentials())
                .build();
    }

    private GoogleCredentials createGoogleCredentials() throws IOException {
        final Resource resource = new ClassPathResource(fcmPrivateKeyPath);
        final List<String> scopes = List.of(fireBaseScope);
        log.debug("resource: {}", resource.getInputStream());
        log.debug("scopes: {}", scopes);

        return GoogleCredentials.fromStream(resource.getInputStream())
                                .createScoped(scopes);
    }
}
