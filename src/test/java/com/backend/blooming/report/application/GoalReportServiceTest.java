package com.backend.blooming.report.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.report.application.exception.InvalidGoalReportException;
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
class GoalReportServiceTest extends GoalReportServiceTestFixture {

    @Autowired
    private GoalReportService goalReportService;

    @Test
    void 골을_신고한다() {
        // when
        final Long actual = goalReportService.create(골_신고_요청_dto);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 이미_신고한_골을_신고하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> goalReportService.create(이미_신고한_사용자가_다시_신고_요청_dto))
                .isInstanceOf(InvalidGoalReportException.AlreadyReportGoalException.class);
    }

    @Test
    void 존재하지_않는_사용자가_골을_신고하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> goalReportService.create(존재하지_않는_사람의_골_신고_요청_dto))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 존재하지_않는_골을_신고하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> goalReportService.create(존재하지_않는_골_신고_요청_dto))
                .isInstanceOf(NotFoundGoalException.class);
    }
}
