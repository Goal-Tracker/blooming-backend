package com.backend.blooming.report.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserReportServiceTest extends UserReportServiceTestFixture {

    @Autowired
    private UserReportService userReportService;

    @Test
    void 사용자를_신고한다() {
        // when
        final Long actual = userReportService.create(사용자_신고_요청_dto);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 사용자를_신고할_때_신고자가_존재하지_않는_사용자인_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> userReportService.create(존재하지_않는_사람의_사용자_신고_요청_dto))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 사용자를_신고할_때_신고_대상자가_존재하지_않는_사용자인_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> userReportService.create(존재하지_않는_사람을_사용자_신고_요청_dto))
                .isInstanceOf(NotFoundUserException.class);
    }
}
