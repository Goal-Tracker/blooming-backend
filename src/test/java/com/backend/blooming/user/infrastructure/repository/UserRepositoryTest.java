package com.backend.blooming.user.infrastructure.repository;

import com.backend.blooming.configuration.JpaConfiguration;
import com.backend.blooming.user.domain.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DataJpaTest
@Import(JpaConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserRepositoryTest extends UserRepositoryTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Test
    void oauth_아이디와_oauth_타입을_통해_사용자를_찾을_수_있다() {
        // when
        final Optional<User> actual = userRepository.findByOAuthIdAndOAuthType(유효한_oAuth_아이디, 유효한_oAuth_타입);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual).contains(사용자);
        });
    }

    @Test
    void 존재하지_않는_oauth_아이디를_통해_사용자를_찾으면_빈_optinal을_반환한다() {
        // when
        final Optional<User> actual = userRepository.findByOAuthIdAndOAuthType(유효하지_않은_oAuth_아이디, 유효한_oAuth_타입);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 존재하는_사용자_아이디_조회시_사용자를_반환한다() {
        // when
        final Optional<User> actual = userRepository.findByIdAndDeletedIsFalse(사용자_아이디);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual).contains(사용자);
        });
    }

    @Test
    void 삭제된_사용자_아이디_조회시_사용자를_반환한다() {
        // when
        final Optional<User> actual = userRepository.findByIdAndDeletedIsFalse(삭제된_사용자_아이디);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 존재하지_않는_사용자_아이디_조회시_사용자를_반환한다() {
        // when
        final Optional<User> actual = userRepository.findByIdAndDeletedIsFalse(존재하지_않는_사용자_아이디);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 존재하는_사용자_아이디_조회시_참을_반환한다() {
        // when
        final boolean actual = userRepository.existsByIdAndDeletedIsFalse(사용자_아이디);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 삭제된_사용자_아이디_조회시_거짓을_반환한다() {
        // when
        final boolean actual = userRepository.existsByIdAndDeletedIsFalse(삭제된_사용자_아이디);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 존재하지_않는_사용자_아이디_조회시_거짓을_반환한다() {
        // when
        final boolean actual = userRepository.existsByIdAndDeletedIsFalse(존재하지_않는_사용자_아이디);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 존재하는_사용자_이름_조회시_참을_반환한다() {
        // when
        final boolean actual = userRepository.existsByNameAndDeletedIsFalse(존재하는_사용자_이름);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 삭제된_사용자_이름_조회시_거짓을_반환한다() {
        // when
        final boolean actual = userRepository.existsByNameAndDeletedIsFalse(삭제된_사용자_이름);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 존재하지_않는_사용자_이름_조회시_거짓을_반환한다() {
        // when
        final boolean actual = userRepository.existsByNameAndDeletedIsFalse(존재하지_않는_사용자_이름);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 요청한_사용자_아이디_리스트에_포함된_모든_사용자를_조회한다() {
        // when
        final List<User> actual = userRepository.findAllByUserIds(사용자_아이디_목록);

        // then
        assertSoftly(SoftAssertions -> {
            assertThat(actual).hasSize(3);
            assertThat(actual.get(0).getId()).isEqualTo(사용자_아이디);
            assertThat(actual.get(0).getName()).isEqualTo(사용자.getName());
            assertThat(actual.get(1).getId()).isEqualTo(사용자2.getId());
            assertThat(actual.get(1).getName()).isEqualTo(사용자2.getName());
            assertThat(actual.get(2).getId()).isEqualTo(삭제된_사용자.getId());
            assertThat(actual.get(2).getName()).isEqualTo(삭제된_사용자.getName());
        });
    }
}
