package com.backend.blooming.report.application.dto;

import com.backend.blooming.report.domain.Category;

import java.util.List;

public record ReadReportCategoriesDto(List<ReadReportCategoryDto> categories) {

    public static ReadReportCategoriesDto from(final List<Category> categories) {
        final List<ReadReportCategoryDto> readReportCategoryDtos = categories.stream()
                                                                             .map(ReadReportCategoryDto::new)
                                                                             .toList();

        return new ReadReportCategoriesDto(readReportCategoryDtos);
    }

    public record ReadReportCategoryDto(Category category) {}
}
