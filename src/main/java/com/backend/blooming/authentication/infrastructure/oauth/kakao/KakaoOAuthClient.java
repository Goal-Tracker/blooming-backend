package com.backend.blooming.authentication.infrastructure.oauth.kakao;

import com.backend.blooming.authentication.configuration.KakaoClientConfigurationProperties;
import com.backend.blooming.authentication.infrastructure.exception.OAuthException;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthClient;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.authentication.infrastructure.oauth.dto.UserInformationDto;
import com.backend.blooming.authentication.infrastructure.oauth.kakao.dto.KakaoUnlinkUserDto;
import com.backend.blooming.authentication.infrastructure.oauth.kakao.dto.KakaoUserInformationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoOAuthClient implements OAuthClient {

    private static final String TOKEN_TYPE = "Bearer ";
    private static final String ADMIN_TOKEN_TYPE = "KakaoAK ";

    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final KakaoClientConfigurationProperties properties;

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
                    properties.userInfoUri(),
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

    @Override
    public Mono<Boolean> unlink(final String oAuthId) {
        return webClient.post()
                        .uri(properties.userUnlinkUri())
                        .headers(header -> {
                            header.set(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN_TYPE + properties.adminKey());
                            header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                        })
                        .body(BodyInserters.fromFormData("target_id_type", "user_id")
                                           .with("target_id", oAuthId))
                        .retrieve()
                        .bodyToMono(KakaoUnlinkUserDto.class)
                        .retry(3)
                        .map(kakaoUnlinkUserDto -> {
                            log.info("UNLIKE USER: {}", kakaoUnlinkUserDto.id());
                            return kakaoUnlinkUserDto.id().equals(oAuthId);
                        })
                        .onErrorResume(this::handleException);
    }

    private Mono<Boolean> handleException(final Throwable exception) {
        if (exception instanceof HttpClientErrorException) {
            throw new OAuthException.InvalidAuthorizationTokenException();
        }
        if (exception instanceof HttpServerErrorException) {
            throw new OAuthException.KakaoServerUnavailableException();
        }

        return Mono.error(exception);
    }
}
