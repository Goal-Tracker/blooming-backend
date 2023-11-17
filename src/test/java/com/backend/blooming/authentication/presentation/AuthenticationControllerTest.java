package com.backend.blooming.authentication.presentation;

import com.backend.blooming.authentication.application.AuthenticationService;
import com.backend.blooming.authentication.application.exception.UnauthorizedAccessException;
import com.backend.blooming.authentication.infrastructure.exception.InvalidTokenException;
import com.backend.blooming.authentication.infrastructure.exception.OAuthException;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticationArgumentResolver;
import com.backend.blooming.authentication.presentation.fixture.AuthenticationControllerTestFixture;
import com.backend.blooming.authentication.presentation.interceptor.AuthenticationInterceptor;
import com.backend.blooming.common.RestDocsConfiguration;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {AuthenticationController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthenticationInterceptor.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthenticationArgumentResolver.class)
        }
)
@Import(RestDocsConfiguration.class)
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthenticationControllerTest extends AuthenticationControllerTestFixture {

    MockMvc mockMvc;

    @Autowired
    AuthenticationController authenticationController;

    @MockBean
    AuthenticationService authenticationService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestDocumentationResultHandler restDocs;

    @Autowired
    RestDocumentationContextProvider restDocumentation;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .apply(documentationConfiguration(restDocumentation))
                                 .alwaysDo(print())
                                 .alwaysDo(restDocs)
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
        ).andDo(restDocs.document(
                pathParameters(parameterWithName("oAuthType").description("소셜 로그인 타입")),
                requestFields(
                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("소셜 access token")
                ),
                responseFields(
                        fieldWithPath("token.accessToken").type(JsonFieldType.STRING).description("서비스 access token"),
                        fieldWithPath("token.refreshToken").type(JsonFieldType.STRING).description("서비스 refresh token"),
                        fieldWithPath("isSignUp").type(JsonFieldType.BOOLEAN).description("첫 로그인 여부")
                )
        ));
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

    @Test
    void refresh_token을_통해_access_token을_재발행한다() throws Exception {
        // given
        given(authenticationService.reissueAccessToken(서비스_refresh_token)).willReturn(서비스_토큰_정보);

        // when & then
        mockMvc.perform(get("/auth/reissue")
                .header("X-API-VERSION", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(access_token_재발급_요청))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.accessToken").value(서비스_토큰_정보.accessToken()),
                jsonPath("$.refreshToken").value(서비스_토큰_정보.refreshToken())
        ).andDo(restDocs.document(
                requestFields(
                        fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("서비스 refresh token")
                ),
                responseFields(
                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("서비스 access token"),
                        fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("서비스 refresh token")
                )
        ));
    }

    @Test
    void 유효하지_않은_refresh_token을_통해_access_token을_재발행시_404를_반환한다() throws Exception {
        // given
        given(authenticationService.reissueAccessToken(서비스_refresh_token))
                .willThrow(new InvalidTokenException("Bearer 타입의 토큰이 아닙니다."));

        // when & then
        mockMvc.perform(get("/auth/reissue")
                .header("X-API-VERSION", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(access_token_재발급_요청))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        );
    }

    @Test
    void 존재하지_않는_사용자의_refresh_token을_통해_access_token을_재발행시_401을_반환한다() throws Exception {
        // given
        given(authenticationService.reissueAccessToken(서비스_refresh_token))
                .willThrow(new UnauthorizedAccessException("권한이 없는 사용자입니다."));

        // when & then
        mockMvc.perform(get("/auth/reissue")
                .header("X-API-VERSION", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(access_token_재발급_요청))
        ).andExpectAll(
                status().isUnauthorized(),
                jsonPath("$.message").exists()
        );
    }
}
