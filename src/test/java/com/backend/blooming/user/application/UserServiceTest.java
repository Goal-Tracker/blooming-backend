package com.backend.blooming.user.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.user.application.dto.UserDto;
import com.backend.blooming.user.application.exception.NotFoundUserException;
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
    void 사용자_조회시_존재하는_사용자_아이디라면_사용자를_조회할_수_있다() {
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
    void 사용자_조회시_삭제한_사용자_아이디라면_빈_optional을_반환한다() {
        // when & then
        assertThatThrownBy(() -> userService.readById(삭제한_사용자_아아디))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 사용자_조회시_존재하는_않는_사용자_아이디라면_조회시_빈_optional을_반환한다() {
        // when & then
        assertThatThrownBy(() -> userService.readById(존재하지_않는_사용자_아아디))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 사용자_정보_수정시_모든_정보를_수정할_수_있다() {
        // when
        final UserDto actual = userService.updateById(사용자_아이디, 모든_사용자_정보를_수정한_dto);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.name()).isEqualTo(수정한_이름);
            softAssertions.assertThat(actual.color()).isEqualTo(수정한_테마_색상.getCode());
            softAssertions.assertThat(actual.statusMessage()).isEqualTo(수정한_상태_메시지);
        });
    }

    @Test
    void 사용자_정보_수정시_이름만_수정할_수_있다() {
        // when
        final UserDto actual = userService.updateById(사용자_아이디, 이름만_수정한_dto);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.name()).isEqualTo(수정한_이름);
            softAssertions.assertThat(actual.color()).isEqualTo(기존_테마_색상.getCode());
            softAssertions.assertThat(actual.statusMessage()).isEqualTo(기존_상태_메시지);
        });
    }

    @Test
    void 사용자_정보_수정시_테마_색상만_수정할_수_있다() {
        // when
        final UserDto actual = userService.updateById(사용자_아이디, 테마_색상만_수정한_dto);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.name()).isEqualTo(기존_이름);
            softAssertions.assertThat(actual.color()).isEqualTo(수정한_테마_색상.getCode());
            softAssertions.assertThat(actual.statusMessage()).isEqualTo(기존_상태_메시지);
        });
    }

    @Test
    void 사용자_정보_수정시_상태_메시지만_수정할_수_있다() {
        // when
        final UserDto actual = userService.updateById(사용자_아이디, 상태_메시지만_수정한_dto);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.name()).isEqualTo(기존_이름);
            softAssertions.assertThat(actual.color()).isEqualTo(기존_테마_색상.getCode());
            softAssertions.assertThat(actual.statusMessage()).isEqualTo(수정한_상태_메시지);
        });
    }

    @Test
    void 사용자_정보_수정시_존재하지_않는_사용자라면_예외를_반환한다() {
        // when & then
        assertThatThrownBy(() -> userService.updateById(존재하지_않는_사용자_아아디, 모든_사용자_정보를_수정한_dto))
                .isInstanceOf(NotFoundUserException.class);
    }
}
