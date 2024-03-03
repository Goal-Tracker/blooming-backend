package com.backend.blooming.report.domain;

import com.backend.blooming.report.application.exception.InvalidGoalReportException;
import com.backend.blooming.report.application.exception.ReportForbiddenException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class GoalReportTest extends GoalReportTestFixture {

    @Test
    void 골_관리자인_사용자가_골을_신고하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new GoalReport(골_관리자, 골, 신고_내용))
                .isInstanceOf(InvalidGoalReportException.NotAllowedReportOwnGoalException.class);
    }

    @Test
    void 팀원이_아닌_사용자가_골을_신고하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new GoalReport(팀원이_아닌_사용자, 골, 신고_내용))
                .isInstanceOf(ReportForbiddenException.GoalReportForbiddenException.class);
    }
}
