package com.backend.blooming.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Profile("prod | dev")
public class FCMConfiguration {

    @Value("${fcm.key.path}")
    private String FCM_PRIVATE_KEY_PATH;

    @Value("${fcm.key.scope}")
    private String fireBaseScope;

    private final ResourceLoader resourceLoader;

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        final FirebaseApp firebaseApp = getFirebaseApp().orElse(FirebaseApp.initializeApp(createFirebaseOptions()));

        return FirebaseMessaging.getInstance(firebaseApp);
    }

    private Optional<FirebaseApp> getFirebaseApp() {
        final List<FirebaseApp> firebaseApps = FirebaseApp.getApps();

        if (firebaseApps == null || firebaseApps.isEmpty()) {
            return Optional.empty();
        }

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
        final Resource resource = resourceLoader.getResource(FCM_PRIVATE_KEY_PATH);
        final List<String> scopes = List.of(fireBaseScope);

        return GoogleCredentials.fromStream(resource.getInputStream())
                                .createScoped(scopes);
    }
}
