package com.backend.blooming.authentication.presentation.dto.response;

import com.backend.blooming.authentication.application.dto.LoginInformationDto;

public record LoginInformationResponse(TokenResponse token, boolean isSignUp) {

    public static LoginInformationResponse from(final LoginInformationDto loginInformationDto) {
        return new LoginInformationResponse(
                TokenResponse.from(loginInformationDto.token()),
                loginInformationDto.isSignUp()
        );
    }
}
