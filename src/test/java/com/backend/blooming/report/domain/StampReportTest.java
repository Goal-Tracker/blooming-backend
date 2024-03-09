package com.backend.blooming.report.domain;

import com.backend.blooming.report.application.exception.InvalidStampReportException;
import com.backend.blooming.report.application.exception.ReportForbiddenException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StampReportTest extends StampReportTestFixture {

    @Test
    void 자신이_작성한_스탬프를_신고하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new StampReport(신고자, 신고자가_생성한_스탬프, 신고_내용))
                .isInstanceOf(InvalidStampReportException.NotAllowedReportOwnStampException.class);
    }

    @Test
    void 팀원이_아닌_사용자가_스탬프를_신고하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new StampReport(팀원이_아닌_사용자, 스탬프, 신고_내용))
                .isInstanceOf(ReportForbiddenException.StampReportForbiddenException.class);
    }
}
