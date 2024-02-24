package com.backend.blooming.report.application.dto;

import com.backend.blooming.report.presentation.dto.request.CreateReportRequest;

public record CreateGoalReportDto(Long reporterId, Long goalId, String content) {

    public static CreateGoalReportDto of(final Long reporterId, final Long goalId, final CreateReportRequest request) {
        return new CreateGoalReportDto(reporterId, goalId, request.content());
    }
}
