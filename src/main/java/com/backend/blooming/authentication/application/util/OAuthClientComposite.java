package com.backend.blooming.authentication.application.util;

import com.backend.blooming.authentication.infrastructure.exception.UnsupportedOAuthTypeException;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthClient;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OAuthClientComposite {

    private final Map<OAuthType, OAuthClient> mapper;

    public OAuthClientComposite(final Set<OAuthClient> oAuthClients) {
        this.mapper = oAuthClients.stream()
                                  .collect(Collectors.toMap(OAuthClient::getOAuthType, oAuthClient -> oAuthClient));
    }

    public OAuthClient findOAuthClient(final OAuthType oAuthType) {
        final OAuthClient oAuthClient = mapper.get(oAuthType);

        if (oAuthClient == null) {
            throw new UnsupportedOAuthTypeException();
        }

        return oAuthClient;
    }
}
