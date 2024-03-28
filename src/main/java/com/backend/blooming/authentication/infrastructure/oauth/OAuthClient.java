package com.backend.blooming.authentication.infrastructure.oauth;

import com.backend.blooming.authentication.infrastructure.oauth.dto.UserInformationDto;
import reactor.core.publisher.Mono;


public interface OAuthClient {

    OAuthType getOAuthType();

    UserInformationDto findUserInformation(final String accessToken);

    Mono<Boolean> unlink(final String oAuthId);
}
