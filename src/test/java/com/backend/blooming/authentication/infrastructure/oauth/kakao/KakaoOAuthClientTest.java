package com.backend.blooming.authentication.infrastructure.oauth.kakao;

import com.backend.blooming.authentication.configuration.AuthenticationPropertiesConfiguration;
import com.backend.blooming.authentication.configuration.KakaoClientConfigurationProperties;
import com.backend.blooming.authentication.configuration.WebClientConfiguration;
import com.backend.blooming.authentication.infrastructure.exception.OAuthException;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.authentication.infrastructure.oauth.dto.UserInformationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest({KakaoOAuthClient.class})
@AutoConfigureWebClient(registerRestTemplate = true)
@Import({AuthenticationPropertiesConfiguration.class, WebClientConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class KakaoOAuthClientTest extends KakaoOAuthClientTestFixture {

    private KakaoOAuthClient kakaoOAuthClient;

    @Autowired
    private MockRestServiceServer kakaoMockServer;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KakaoClientConfigurationProperties properties;

    private MockWebServer mockWebServer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final WebClient webClient = WebClient.create(mockWebServer.url("/").toString());

        kakaoOAuthClient = new KakaoOAuthClient(restTemplate, webClient, properties);
    }

    @AfterEach
    void terminate() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void 소셜_로그인_타입을_요청하면_카카오_타입을_반환한다() {
        // when
        final OAuthType actual = kakaoOAuthClient.getOAuthType();

        // then
        assertThat(actual).isEqualTo(OAuthType.KAKAO);
    }

    @Test
    void 카카오에_유효한_토큰_전달시_사용자_정보를_받는다() throws JsonProcessingException {
        // given
        kakaoMockServer.expect(requestTo(사용자_정보_요청_url))
                       .andExpect(method(HttpMethod.GET))
                       .andExpect(header(HttpHeaders.AUTHORIZATION, 사용자_요청_헤더))
                       .andRespond(withSuccess(objectMapper.writeValueAsString(사용자_정보), MediaType.APPLICATION_JSON));

        // when
        final UserInformationDto actual = kakaoOAuthClient.findUserInformation(사용자_ACCESS_TOKEN);

        // then
        assertThat(actual).isEqualTo(사용자_정보);
    }

    @Test
    void 카카오에_유효하지_않는_토큰_전달시_예외를_반환한다() {
        // given
        kakaoMockServer.expect(requestTo(사용자_정보_요청_url))
                       .andExpect(method(HttpMethod.GET))
                       .andExpect(header(HttpHeaders.AUTHORIZATION, 사용자_요청_헤더))
                       .andRespond(withBadRequest());

        // when & then
        assertThatThrownBy(() -> kakaoOAuthClient.findUserInformation(사용자_ACCESS_TOKEN))
                .isInstanceOf(OAuthException.class);
    }

    @Test
    void 카카오에_유효한_토큰_전달시_서버에_문제가_생긴_경우_예외를_반환한다() {
        // given
        kakaoMockServer.expect(requestTo(사용자_정보_요청_url))
                       .andExpect(method(HttpMethod.GET))
                       .andExpect(header(HttpHeaders.AUTHORIZATION, 사용자_유효하지_않은_요청_헤더))
                       .andRespond(withServerError());

        // when & then
        assertThatThrownBy(() -> kakaoOAuthClient.findUserInformation(사용자_유효하지_않은_ACCESS_TOKEN))
                .isInstanceOf(OAuthException.class);
    }

    @Test
    void 카카오에_oauthId를_전달해_연결을_끊는다() throws IOException, InterruptedException {
        // given
        mockWebServer.enqueue(
                new MockResponse().setResponseCode(HttpStatus.OK.value())
                                  .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                  .setBody(objectMapper.writeValueAsString(연결을_끊은_사용자_정보))
        );

        // when
        final Mono<Boolean> actual = kakaoOAuthClient.unlink(oAuthId);

        // then
        StepVerifier.create(actual)
                    .expectNext(true)
                    .verifyComplete();
        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.POST);
            softAssertions.assertThat(recordedRequest.getPath()).isEqualTo(사용자_연결_끊기_요청_url);
        });
    }
}
