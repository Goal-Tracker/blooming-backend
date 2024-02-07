package com.backend.blooming.admin.controller.dto;

import jakarta.validation.constraints.NotEmpty;

public record CreateUserRequest(
        @NotEmpty(message = "사용자 이름을 입력되지 않았습니다.")
        String name,
        String email,
        String theme,
        String statusMessage
) {
}
