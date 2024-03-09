package com.backend.blooming.report.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedThreadLocal;
import com.backend.blooming.common.RestDocsConfiguration;
import com.backend.blooming.report.application.GoalReportService;
import com.backend.blooming.report.application.StampReportService;
import com.backend.blooming.report.application.UserReportService;
import com.backend.blooming.report.application.exception.InvalidGoalReportException;
import com.backend.blooming.report.application.exception.InvalidStampReportException;
import com.backend.blooming.report.application.exception.InvalidUserReportException;
import com.backend.blooming.report.application.exception.ReportForbiddenException;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
@Import({RestDocsConfiguration.class, AuthenticatedThreadLocal.class})
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReportControllerTest extends ReportControllerTestFixture {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserReportService userReportService;

    @MockBean
    private GoalReportService goalReportService;

    @MockBean
    private StampReportService stampReportService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestDocumentationResultHandler restDocs;

    @Test
    void 사용자를_신고한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(userReportService.create(사용자_신고_요청_dto)).willReturn(생성된_신고_아이디);

        // when & then
        mockMvc.perform(post("/reports/users/{userId}", 신고_대상_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(신고_요청))
        ).andExpectAll(
                status().isNoContent()
        ).andDo(print()).andDo(restDocs.document(
                requestHeaders(
                        headerWithName("X-API-VERSION").description("요청 버전"),
                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                ),
                pathParameters(parameterWithName("userId").description("신고할 사용자 아이디")),
                requestFields(
                        fieldWithPath("content").type(JsonFieldType.STRING).description("신고 내용")
                )
        ));
    }

    @Test
    void 사용자_신고시_내용이_비어있다면_400을_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);

        // when & then
        mockMvc.perform(post("/reports/users/{userId}", 신고_대상_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(내용이_없는_신고_요청))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 사용자_신고시_적절하지_않은_요청이라면_400을_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(userReportService.create(본인_신고_요청_dto))
                .willThrow(new InvalidUserReportException.NotAllowedReportOwnUserException());

        // when & then
        mockMvc.perform(post("/reports/users/{userId}", 사용자_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(신고_요청))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 골을_신고한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(goalReportService.create(골_신고_요청_dto)).willReturn(생성된_신고_아이디);

        // when & then
        mockMvc.perform(post("/reports/goals/{goalId}", 신고_대상_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(신고_요청))
        ).andExpectAll(
                status().isNoContent()
        ).andDo(print()).andDo(restDocs.document(
                requestHeaders(
                        headerWithName("X-API-VERSION").description("요청 버전"),
                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                ),
                pathParameters(parameterWithName("goalId").description("신고할 골 아이디")),
                requestFields(
                        fieldWithPath("content").type(JsonFieldType.STRING).description("신고 내용")
                )
        ));
    }

    @Test
    void 골을_신고시_내용이_비어있다면_400을_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);

        // when & then
        mockMvc.perform(post("/reports/goals/{goalId}", 신고_대상_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(내용이_없는_신고_요청))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 골을_신고시_적절하지_않은_요청이라면_400을_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(goalReportService.create(관리자인_골_신고_요청_dto))
                .willThrow(new InvalidGoalReportException.NotAllowedReportOwnGoalException());

        // when & then
        mockMvc.perform(post("/reports/goals/{goalId}", 신고_대상_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(신고_요청))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 골을_신고시_팀원이_아닌_사용자의_요청이라면_403을_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(팀원이_아닌_사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(팀원이_아닌_사용자_아이디)).willReturn(true);
        given(goalReportService.create(팀원이_아닌_사용자의_골_신고_요청_dto))
                .willThrow(new ReportForbiddenException.GoalReportForbiddenException());

        // when & then
        mockMvc.perform(post("/reports/goals/{goalId}", 신고_대상_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(신고_요청))
        ).andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 스탬프를_신고한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(stampReportService.create(스탬프_신고_요청_dto)).willReturn(생성된_신고_아이디);

        // when & then
        mockMvc.perform(post("/reports/stamps/{stampId}", 신고_대상_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(신고_요청))
        ).andExpectAll(
                status().isNoContent()
        ).andDo(print()).andDo(restDocs.document(
                requestHeaders(
                        headerWithName("X-API-VERSION").description("요청 버전"),
                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                ),
                pathParameters(parameterWithName("stampId").description("신고할 스탬프 아이디")),
                requestFields(
                        fieldWithPath("content").type(JsonFieldType.STRING).description("신고 내용")
                )
        ));
    }

    @Test
    void 스탬프_신고시_내용이_비어있다면_400을_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);

        // when & then
        mockMvc.perform(post("/reports/stamps/{stampId}", 신고_대상_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(내용이_없는_신고_요청))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 스탬프_신고시_적절하지_않은_요청이라면_400을_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(stampReportService.create(본인의_스탬프_신고_요청_dto))
                .willThrow(new InvalidStampReportException.NotAllowedReportOwnStampException());

        // when & then
        mockMvc.perform(post("/reports/stamps/{stampId}", 신고_대상_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(신고_요청))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 스탬프_신고시_팀원이_아닌_사용자의_요청이라면_403을_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(팀원이_아닌_사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(팀원이_아닌_사용자_아이디)).willReturn(true);
        given(stampReportService.create(팀원이_아닌_사용자의_스탬프_신고_요청_dto))
                .willThrow(new ReportForbiddenException.StampReportForbiddenException());

        // when & then
        mockMvc.perform(post("/reports/stamps/{stampId}", 신고_대상_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(신고_요청))
        ).andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }
}
