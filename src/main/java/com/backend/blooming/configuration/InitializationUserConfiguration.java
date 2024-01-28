package com.backend.blooming.configuration;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
@Profile("!test")
public class InitializationUserConfiguration implements ApplicationRunner {

    private final UserRepository userRepository;
    private final Random random = new Random();

    @Override
    @Transactional
    public void run(final ApplicationArguments args) {
        List<User> users = new ArrayList<>();
        final ThemeColor[] themeColors = ThemeColor.values();

        for (int i = 1; i < 31; i++) {
            users.add(
                    User.builder()
                        .oAuthId("1234" + i)
                        .oAuthType(OAuthType.KAKAO)
                        .name(new Name("테스트" + i))
                        .email(new Email("test" + i + "@naver.com"))
                        .color(themeColors[random.nextInt(themeColors.length)])
                        .build()
            );
        }

        userRepository.saveAll(users);
    }
}
