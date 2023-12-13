package com.backend.blooming.authentication.infrastructure.oauth;

import com.backend.blooming.authentication.infrastructure.oauth.dto.UserInformationDto;


public interface OAuthClient {

    OAuthType getOAuthType();

    UserInformationDto findUserInformation(final String accessToken);
}
