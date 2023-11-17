package com.backend.blooming.authentication.infrastructure.oauth.kakao.dto;

import com.backend.blooming.authentication.infrastructure.oauth.dto.UserInformationDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInformationDto(
        @JsonProperty("id")
        String oAuthId,

        @JsonProperty("kakao_account.email")
        String email
) implements UserInformationDto {
}
