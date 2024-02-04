package com.backend.blooming.authentication.application;

import com.backend.blooming.authentication.application.exception.AlreadyRegisterBlackListTokenException;
import com.backend.blooming.configuration.IsolateDatabase;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BlackListTokenServiceTest extends BlackListTokenServiceTestFixture {

    @Autowired
    private BlackListTokenService blackListTokenService;

    @Test
    void 블랙_리스트_토큰을_등록한다() {
        // when
        final Long actual = blackListTokenService.register(토큰);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 블랙_리스트_토큰_등록시_이미_등록된_토큰이라면_예외를_반환한다() {
        // when & then
        assertThatThrownBy(() -> blackListTokenService.register(이미_등록된_토큰))
                .isInstanceOf(AlreadyRegisterBlackListTokenException.class);
    }
}
