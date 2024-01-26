package com.backend.blooming.notification.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedThreadLocal;
import com.backend.blooming.common.RestDocsConfiguration;
import com.backend.blooming.notification.application.NotificationService;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@Import({RestDocsConfiguration.class, AuthenticatedThreadLocal.class})
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NotificationControllerTest extends NotificationControllerTestFixture {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenProvider tokenProvider;

    @Autowired
    private RestDocumentationResultHandler restDocs;

    @Test
    void 사용자의_알림_목록을_조회한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(notificationService.readAllByUserId(사용자_아이디)).willReturn(알림_목록_정보_dto);

        // when & then
        mockMvc.perform(get("/notifications")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.notifications.[0].id", is(알림_정보_dto1.id()), Long.class),
                jsonPath("$.notifications.[0].title", is(알림_정보_dto1.title())),
                jsonPath("$.notifications.[0].content", is(알림_정보_dto1.content())),
                jsonPath("$.notifications.[0].type", is(알림_정보_dto1.type().name())),
                jsonPath("$.notifications.[0].requestId", is(알림_정보_dto1.requestId()), Long.class),
                jsonPath("$.notifications.[1].id", is(알림_정보_dto2.id()), Long.class),
                jsonPath("$.notifications.[1].title", is(알림_정보_dto2.title())),
                jsonPath("$.notifications.[1].content", is(알림_정보_dto2.content())),
                jsonPath("$.notifications.[1].type", is(알림_정보_dto2.type().name())),
                jsonPath("$.notifications.[1].requestId", is(알림_정보_dto2.requestId()), Long.class)
        ).andDo(print()).andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("X-API-VERSION").description("요청 버전"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("notifications").type(JsonFieldType.ARRAY).description("사용자의 알림 목록"),
                                fieldWithPath("notifications.[].id").type(JsonFieldType.NUMBER).description("알림 아이디"),
                                fieldWithPath("notifications.[].title").type(JsonFieldType.STRING).description("알림 제목"),
                                fieldWithPath("notifications.[].content").type(JsonFieldType.STRING)
                                                                         .description("알림 내용"),
                                fieldWithPath("notifications.[].type").type(JsonFieldType.STRING).description("알림 타입"),
                                fieldWithPath("notifications.[].requestId").type(JsonFieldType.NUMBER)
                                                                           .description("알림 수락 시 처리를 위한 아이디")
                        )
                )
        );
    }
}
