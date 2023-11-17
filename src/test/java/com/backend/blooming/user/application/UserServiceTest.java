package com.backend.blooming.user.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.user.application.dto.UserDto;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.application.fixture.UserServiceTestFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserServiceTest extends UserServiceTestFixture {

    @Autowired
    UserService userService;

    @Test
    void 존재하는_사용자_아이디를_통해_사용자를_조회한다() {
        // when
        final UserDto actual = userService.readById(사용자_아이디);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.id()).isPositive();
            softAssertions.assertThat(actual.oAuthId()).isEqualTo(사용자.getOAuthId());
            softAssertions.assertThat(actual.oAuthType()).isEqualTo(사용자.getOAuthType().name());
            softAssertions.assertThat(actual.name()).isEqualTo(사용자.getName());
        });
    }

    @Test
    void 삭제한_사용자_아이디를_통해_사용자_조회시_빈_optional을_반환한다() {
        // when & then
        assertThatThrownBy(() -> userService.readById(삭제한_사용자_아아디))
                .isInstanceOf(NotFoundUserException.class)
                .hasMessage("사용자를 조회할 수 없습니다.");
    }

    @Test
    void 존재하는_않는_사용자_아이디를_통해_사용자_조회시_빈_optional을_반환한다() {
        // when & then
        assertThatThrownBy(() -> userService.readById(존재하지_않는_사용자_아아디))
                .isInstanceOf(NotFoundUserException.class)
                .hasMessage("사용자를 조회할 수 없습니다.");
    }
}
