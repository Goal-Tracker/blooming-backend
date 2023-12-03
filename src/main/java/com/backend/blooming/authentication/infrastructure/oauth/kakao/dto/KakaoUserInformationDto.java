package com.backend.blooming.authentication.infrastructure.oauth.kakao.dto;

import com.backend.blooming.authentication.infrastructure.oauth.dto.UserInformationDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInformationDto(
        @JsonProperty("id")
        String oAuthId,

        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) implements UserInformationDto {

    public String email() {
        return kakaoAccount().email();
    }

    public record KakaoAccount(
            @JsonProperty("email")
            String email
    ) {
    }
}
