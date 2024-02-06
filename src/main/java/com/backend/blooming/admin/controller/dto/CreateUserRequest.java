package com.backend.blooming.admin.controller.dto;

import jakarta.validation.constraints.NotEmpty;

public record CreateUserRequest(@NotEmpty String name, String email, String theme, String statusMessage) {
}
