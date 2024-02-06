package com.backend.blooming.admin.application;

import com.backend.blooming.configuration.IsolateDatabase;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminPageServiceTest extends AdminPageServiceTestFixture {

    @Autowired
    private AdminPageService adminPageService;

    @Test
    void 사용자를_생성한다() {
        // when
        final Long actual = adminPageService.createUser(사용자_생성_요청);

        // then
        assertThat(actual).isPositive();
    }
}
