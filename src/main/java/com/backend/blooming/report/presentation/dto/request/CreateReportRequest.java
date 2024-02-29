package com.backend.blooming.report.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReportRequest(
        @NotNull(message = "신고 내용은 비어있을 수 없습니다.")
        @NotBlank(message = "신고 내용은 비어있을 수 없습니다.")
        String content
) {
}
