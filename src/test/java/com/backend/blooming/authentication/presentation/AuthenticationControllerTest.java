package com.backend.blooming.authentication.presentation;

import com.backend.blooming.authentication.application.AuthenticationService;
import com.backend.blooming.authentication.infrastructure.exception.OAuthException;
import com.backend.blooming.authentication.presentation.fixture.AuthenticationControllerTestFixture;
import com.backend.blooming.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthenticationControllerTest extends AuthenticationControllerTestFixture {

    MockMvc mockMvc;

    @Autowired
    AuthenticationController authenticationController;

    @MockBean
    AuthenticationService authenticationService;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .alwaysDo(print())
                                 .build();
    }

    @Test
    void oauth_access_token을_통해_로그인시_첫_로그인이라면_회원가입_여부를_참으로_반환한다() throws Exception {
        // given
        given(authenticationService.login(oauth_타입, 소셜_액세스_토큰)).willReturn(소셜_로그인_사용자_정보);

        // when & then
        mockMvc.perform(get("/auth/login/oauth/{oAuthType}", oauth_타입.name().toLowerCase())
                .header("X-API-VERSION", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(소셜_로그인_정보))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.token.accessToken").value(소셜_로그인_사용자_정보.token().accessToken()),
                jsonPath("$.token.refreshToken").value(소셜_로그인_사용자_정보.token().refreshToken()),
                jsonPath("$.isSignUp").value(true)
        );
    }

    @Test
    void oauth_access_token을_통해_로그인시_기존_회원이라면_회원가입_여부를_거짓으로_반환한다() throws Exception {
        // given
        given(authenticationService.login(oauth_타입, 소셜_액세스_토큰)).willReturn(소셜_로그인_기존_사용자_정보);

        // when & then
        mockMvc.perform(get("/auth/login/oauth/{oAuthType}", oauth_타입.name().toLowerCase())
                .header("X-API-VERSION", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(소셜_로그인_정보))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.token.accessToken").value(소셜_로그인_기존_사용자_정보.token().accessToken()),
                jsonPath("$.token.refreshToken").value(소셜_로그인_기존_사용자_정보.token().refreshToken()),
                jsonPath("$.isSignUp").value(false)
        );
    }

    @Test
    void oauth_access_token을_통해_로그인시_유효하지_않은_토큰이라면_403을_반환한다() throws Exception {
        // given
        given(authenticationService.login(oauth_타입, 유효하지_않은_소셜_액세스_토큰))
                .willThrow(new OAuthException.InvalidAuthorizationTokenException("유효하지 않은 토큰입니다."));

        // when & then
        mockMvc.perform(get("/auth/login/oauth/{oAuthType}", oauth_타입.name().toLowerCase())
                .header("X-API-VERSION", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(유효하지_않은_소셜_로그인_정보))
        ).andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").exists()
        );
    }

    @Test
    void oauth_access_token을_통해_로그인시_해당_소셜_서버에_문제가_생겼다면_503을_반환한다() throws Exception {
        // given
        given(authenticationService.login(oauth_타입, 소셜_액세스_토큰))
                .willThrow(new OAuthException.KakaoServerException("카카오 서버에 문제가 발생했습니다."));

        // when & then
        mockMvc.perform(get("/auth/login/oauth/{oAuthType}", oauth_타입.name().toLowerCase())
                .header("X-API-VERSION", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(소셜_로그인_정보))
        ).andExpectAll(
                status().isServiceUnavailable(),
                jsonPath("$.message").exists()
        );
    }
}
