package com.backend.blooming.report.application;

import com.backend.blooming.report.application.dto.ReadReportCategoriesDto;
import com.backend.blooming.report.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
// TODO: 2024-02-20 ReportService와 ReportCategoryService 중 뭐가 더 적절할까요?
public class ReportService {

    public ReadReportCategoriesDto readAllCategory() {
        return ReadReportCategoriesDto.from(List.of(Category.values()));
    }
}
