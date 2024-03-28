package com.backend.blooming.authentication.infrastructure.oauth.kakao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record KakaoUnlinkUserDto(@NotNull @NotBlank String id) {
}
