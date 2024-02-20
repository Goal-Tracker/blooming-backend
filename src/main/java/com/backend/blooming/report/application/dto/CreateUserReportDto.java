package com.backend.blooming.report.application.dto;

public record CreateUserReportDto(Long reporterId, Long reporteeId, String content) {
}
