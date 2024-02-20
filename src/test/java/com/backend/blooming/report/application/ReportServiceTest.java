package com.backend.blooming.report.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.report.application.dto.ReadReportCategoriesDto;
import com.backend.blooming.report.domain.Category;
import org.assertj.core.api.*;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Test
    void 신고_카테고리를_조회한다() {
        // when
        final ReadReportCategoriesDto actual = reportService.readAllCategory();

        // then
        final Category[] categoryValues = Category.values();
        final int categoryLength = categoryValues.length;
        final List<Category> categories = List.of(categoryValues);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.categories()).hasSize(categoryLength);
            for (int i = 0; i < categoryLength; i++) {
                softAssertions.assertThat(actual.categories().get(i).category()).isIn(categories);
            }
        });
    }
}
