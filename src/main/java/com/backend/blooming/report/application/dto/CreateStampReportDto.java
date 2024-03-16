package com.backend.blooming.report.application.dto;

import com.backend.blooming.report.presentation.dto.request.CreateReportRequest;

public record CreateStampReportDto(Long reporterId, Long stampId, String content) {

    public static CreateStampReportDto of(final Long reporterId, final Long stampId, final CreateReportRequest request) {
        return new CreateStampReportDto(reporterId, stampId, request.content());
    }
}
