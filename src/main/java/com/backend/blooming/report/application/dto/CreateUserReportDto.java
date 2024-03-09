package com.backend.blooming.report.application.dto;

import com.backend.blooming.report.presentation.dto.request.CreateReportRequest;

public record CreateUserReportDto(Long reporterId, Long reporteeId, String content) {

    public static CreateUserReportDto of(final Long reporterId, final Long reporteeId, final CreateReportRequest request) {
        return new CreateUserReportDto(reporterId, reporteeId, request.content());
    }
}
