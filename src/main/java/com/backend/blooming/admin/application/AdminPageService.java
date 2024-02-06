package com.backend.blooming.admin.application;

import com.backend.blooming.admin.controller.dto.CreateUserRequest;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminPageService {

    private static final String DEFAULT_EMAIL = "test@email.com";
    private final UserRepository userRepository;

    public Long createUser(final CreateUserRequest request) {
        final User user = User.builder()
                              .oAuthId(UUID.randomUUID().toString())
                              .oAuthType(OAuthType.KAKAO)
                              .name(new Name(request.name()))
                              .email(processEmail(request.email()))
                              .color(ThemeColor.valueOf(request.theme()))
                              .statusMessage(request.statusMessage())
                              .build();

        return userRepository.save(user)
                             .getId();
    }

    private Email processEmail(final String email) {
        if (email.isEmpty()) {
            return new Email(DEFAULT_EMAIL);
        }

        return new Email(email);
    }
}
