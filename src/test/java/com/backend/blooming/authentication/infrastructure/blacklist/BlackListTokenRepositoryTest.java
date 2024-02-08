package com.backend.blooming.authentication.infrastructure.blacklist;

import com.backend.blooming.authentication.domain.BlackListToken;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BlackListTokenRepositoryTest extends BlackListTokenRepositoryTestFixture {

    @Autowired
    private BlackListTokenRepository blackListTokenRepository;

    @Test
    void 블랙_리스트에_특정_토큰이_존재한다면_해당_토큰을_반환한다() {
        // when
        final Optional<BlackListToken> actual = blackListTokenRepository.findByToken(블랙_리스트_토큰.getToken());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get().getToken()).isEqualTo(블랙_리스트_토큰.getToken());
        });
    }

    @Test
    void 블랙_리스트에_특정_토큰이_존재한다면_참을_반환한다() {
        // when
        final boolean actual = blackListTokenRepository.existsByToken(블랙_리스트_토큰.getToken());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 블랙_리스트에_특정_토큰이_존재하지_않다면_거짓을_반환한다() {
        // when
        final boolean actual = blackListTokenRepository.existsByToken(존재하지_않는_토큰);

        // then
        assertThat(actual).isFalse();
    }
}
