package com.backend.blooming.themecolor.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedThreadLocal;
import com.backend.blooming.common.RestDocsConfiguration;
import com.backend.blooming.themecolor.application.ThemeColorService;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ThemeColorController.class)
@Import({RestDocsConfiguration.class, AuthenticatedThreadLocal.class})
@MockBean({TokenProvider.class, UserRepository.class})
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ThemeColorControllerTest extends ThemeColorControllerTestFixture {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ThemeColorService themeColorService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestDocumentationResultHandler restDocs;

    @Test
    void 전체_테마_색상_목록을_조회한다() throws Exception {
        // given
        given(themeColorService.readAll()).willReturn(테마_색상_목록_dto);

        // when & then
        mockMvc.perform(get("/theme-colors")
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
