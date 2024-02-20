package com.backend.blooming.report.domain;

import com.backend.blooming.report.domain.exception.InvalidReportException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReportInformationTest extends ReportInformationTestFixture {

    @Test
    void 신고_정보_생성시_신고자와_신고_대상자가_동일한_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new ReportInformation(신고자, 신고자, 신고_내용))
                .isInstanceOf(InvalidReportException.SelfReportingNotAllowedException.class);
    }
}
