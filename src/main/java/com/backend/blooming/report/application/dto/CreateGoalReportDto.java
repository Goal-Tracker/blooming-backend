package com.backend.blooming.report.application.dto;

public record CreateGoalReportDto(Long reporterId, Long goalId, String content) {
}
