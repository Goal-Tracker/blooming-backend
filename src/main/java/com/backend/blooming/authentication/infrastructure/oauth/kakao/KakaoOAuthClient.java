package com.backend.blooming.authentication.infrastructure.oauth.kakao;

import com.backend.blooming.authentication.configuration.KakaoClientConfigurationProperties;
import com.backend.blooming.authentication.infrastructure.exception.OAuthException;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthClient;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.authentication.infrastructure.oauth.dto.UserInformationDto;
import com.backend.blooming.authentication.infrastructure.oauth.kakao.dto.KakaoUserInformationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoOAuthClient implements OAuthClient {

    private static final String TOKEN_TYPE = "Bearer ";

    private final RestTemplate restTemplate;
    private final KakaoClientConfigurationProperties configurationProperties;

    @Override
    public OAuthType getOAuthType() {
        return OAuthType.KAKAO;
    }

    @Override
    public UserInformationDto findUserInformation(final String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + accessToken);
        HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);

        try {
            final ResponseEntity<KakaoUserInformationDto> response = restTemplate.exchange(
                    configurationProperties.userInfoUri(),
                    HttpMethod.GET,
                    request,
                    KakaoUserInformationDto.class
            );

            return response.getBody();
        } catch (final HttpClientErrorException exception) {
            throw new OAuthException.InvalidAuthorizationTokenException();
        } catch (final HttpServerErrorException exception) {
            throw new OAuthException.KakaoServerUnavailableException();
        }
    }
}
