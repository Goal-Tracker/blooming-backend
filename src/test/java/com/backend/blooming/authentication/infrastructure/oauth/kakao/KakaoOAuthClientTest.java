package com.backend.blooming.authentication.infrastructure.oauth.kakao;

import com.backend.blooming.authentication.configuration.AuthenticationPropertiesConfiguration;
import com.backend.blooming.authentication.infrastructure.exception.OAuthException;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.authentication.infrastructure.oauth.dto.UserInformationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

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
@Import({AuthenticationPropertiesConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class KakaoOAuthClientTest extends KakaoOAuthClientTestFixture {

    @Autowired
    private KakaoOAuthClient kakaoOAuthClient;

    @Autowired
    private MockRestServiceServer kakaoMockServer;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
}
