package com.backend.blooming.themecolor.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedThreadLocal;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticationArgumentResolver;
import com.backend.blooming.authentication.presentation.interceptor.AuthenticationInterceptor;
import com.backend.blooming.common.RestDocsConfiguration;
import com.backend.blooming.exception.GlobalExceptionHandler;
import com.backend.blooming.themecolor.application.ThemeColorService;
import com.backend.blooming.themecolor.presentation.fixture.ThemeColorControllerTestFixture;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {ThemeColorController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class)
        }
)
@Import({RestDocsConfiguration.class})
@MockBean({AuthenticatedThreadLocal.class})
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ThemeColorControllerTest extends ThemeColorControllerTestFixture {

    MockMvc mockMvc;

    @Autowired
    ThemeColorController themeColorController;

    @MockBean
    ThemeColorService themeColorService;

    @Autowired
    AuthenticationArgumentResolver authenticationArgumentResolver;

    @Autowired
    AuthenticationInterceptor authenticationInterceptor;

    @MockBean
    UserRepository userRepository;

    @MockBean
    TokenProvider tokenProvider;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestDocumentationResultHandler restDocs;

    @Autowired
    RestDocumentationContextProvider restDocumentation;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(themeColorController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .setCustomArgumentResolvers(authenticationArgumentResolver)
                                 .addInterceptors(authenticationInterceptor)
                                 .apply(documentationConfiguration(restDocumentation))
                                 .alwaysDo(print())
                                 .alwaysDo(restDocs)
                                 .build();
    }

    @Test
    void 전체_테마_색상_목록을_조회한다() throws Exception {
        // given
        given(themeColorService.readAll()).willReturn(테마_색상_목록_dto);

        // when & then
        mockMvc.perform(get("/theme-color")
                .header("X-API-VERSION", 1)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.themeColors[0].name").exists(),
                jsonPath("$.themeColors[0].code").exists()
        ).andDo(restDocs.document(
                requestHeaders(headerWithName("X-API-VERSION").description("요청 버전")),
                responseFields(
                        fieldWithPath("themeColors.[]").type(JsonFieldType.ARRAY).description("테마 색상 목록"),
                        fieldWithPath("themeColors.[].name").type(JsonFieldType.STRING).description("테마 색상 이름"),
                        fieldWithPath("themeColors.[].code").type(JsonFieldType.STRING).description("테마 색상 코드")
                )
        ));
    }
}
