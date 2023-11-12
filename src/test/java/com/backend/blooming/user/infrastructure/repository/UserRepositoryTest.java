package com.backend.blooming.user.infrastructure.repository;

import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.fixture.UserRepositoryTestFixture;
import org.assertj.core.api.*;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserRepositoryTest extends UserRepositoryTestFixture {

    @Autowired
    UserRepository userRepository;

    @Test
    void oauth_아이디와_oauth_타입을_통해_사용자를_찾을_수_있다() {
        // when
        final Optional<User> actual = userRepository.findByOAuthIdAndOAuthType(유효한_oAuth_아이디, 유효한_oAuth_타입);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual).contains(사용자);
        });
    }

    @Test
    void 존재하지_않는_oauth_아이디를_통해_사용자를_찾으면_반_optinal을_반환한다() {
        // when
        final Optional<User> actual = userRepository.findByOAuthIdAndOAuthType(유효하지_않은_oAuth_아이디, 유효한_oAuth_타입);

        // then
        assertThat(actual).isEmpty();
    }
}
