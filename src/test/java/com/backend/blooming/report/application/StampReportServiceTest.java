package com.backend.blooming.report.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.report.application.exception.InvalidStampReportException;
import com.backend.blooming.stamp.application.exception.NotFoundStampException;
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
class StampReportServiceTest extends StampReportServiceTestFixture {

    @Autowired
    private StampReportService stampReportService;

    @Test
    void 스탬프를_신고한다() {
        // when
        final Long actual = stampReportService.create(스탬프_신고_요청_dto);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 이미_신고한_사용자가_스탬프를_신고하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> stampReportService.create(이미_신고한_사용자가_스탬프_신고_요청_dto))
                .isInstanceOf(InvalidStampReportException.AlreadyReportStampException.class);
    }

    @Test
    void 존재하지_않는_사용자가_스탬프를_신고하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> stampReportService.create(존재하지_않는_사용자가_스탬프_신고_요청_dto))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 존재하지_않는_스탬프를_신고하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> stampReportService.create(존재하지_않는_스탬프_신고_요청_dto))
                .isInstanceOf(NotFoundStampException.class);
    }
}
