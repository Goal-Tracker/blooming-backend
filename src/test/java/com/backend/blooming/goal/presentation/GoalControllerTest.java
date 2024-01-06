package com.backend.blooming.goal.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedThreadLocal;
import com.backend.blooming.common.RestDocsConfiguration;
import com.backend.blooming.goal.application.GoalService;
import com.backend.blooming.goal.application.exception.InvalidGoalException;
import com.backend.blooming.user.application.exception.NotFoundUserException;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GoalController.class)
@AutoConfigureRestDocs
@Import({RestDocsConfiguration.class, AuthenticatedThreadLocal.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class GoalControllerTest extends GoalControllerTestFixture {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GoalService goalService;

    @Autowired
    private RestDocumentationResultHandler restDocs;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void 골_생성을_요청하면_새로운_골을_생성한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.createGoal(유효한_골_생성_dto)).willReturn(유효한_골_아이디);

        // when & then
        mockMvc.perform(post("/goals/add")
                .header("X-API-VERSION", "1")
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(요청한_골_dto))
        ).andExpectAll(
                status().isCreated(),
                redirectedUrl("/goals/" + 유효한_골_아이디)
        ).andDo(print()).andDo(restDocs.document(
                requestHeaders(
                        headerWithName("X-API-VERSION").description("요청 버전"),
                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("골 제목"),
                        fieldWithPath("memo").type(JsonFieldType.STRING).description("골 메모"),
                        fieldWithPath("startDate").type(JsonFieldType.STRING).description("골 시작날짜"),
                        fieldWithPath("endDate").type(JsonFieldType.STRING).description("골 종료날짜"),
                        fieldWithPath("teamUserIds").type(JsonFieldType.ARRAY).description("골 팀 사용자 아이디")
                )
        ));
    }

    @Test
    void 골_생성시_존재하지_않는_사용자가_참여자로_있는_경우_404_예외를_발생시킨다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.createGoal(존재하지_않는_사용자가_참여자로_있는_골_생성_dto)).willThrow(new NotFoundUserException());

        // when & then
        mockMvc.perform(post("/goals/add")
                .header("X-API-VERSION", "1")
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(존재하지_않는_사용자가_참여자로_있는_골_생성_dto))
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 골_시작날짜가_현재보다_이전인_경우_403_예외를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.createGoal(골_시작날짜가_현재보다_이전인_골_생성_dto)).willThrow(new InvalidGoalException.InvalidInvalidGoalStartDay());

        // when & then
        mockMvc.perform(post("/goals/add")
                .header("X-API-VERSION", "1")
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(골_시작날짜가_현재보다_이전인_골_생성_dto))
        ).andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 골_종료날짜가_현재보다_이전인_경우_403_예외를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.createGoal(골_종료날짜가_현재보다_이전인_골_생성_dto)).willThrow(new InvalidGoalException.InvalidInvalidGoalEndDay());

        // when & then
        mockMvc.perform(post("/goals/add")
                .header("X-API-VERSION", "1")
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(골_종료날짜가_현재보다_이전인_골_생성_dto))
        ).andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 골_종료날짜가_시작날짜보다_이전인_경우_403_예외를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.createGoal(골_종료날짜가_시작날짜보다_이전인_골_생성_dto)).willThrow(new InvalidGoalException.InvalidInvalidGoalPeriod());

        // when & then
        mockMvc.perform(post("/goals/add")
                .header("X-API-VERSION", "1")
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(골_종료날짜가_시작날짜보다_이전인_골_생성_dto))
        ).andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 골_날짜가_1_미만인_경우_403_예외를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.createGoal(골_날짜수가_1_미만인_골_생성_dto)).willThrow(new InvalidGoalException.InvalidInvalidGoalDays());

        // when & then
        mockMvc.perform(post("/goals/add")
                .header("X-API-VERSION", "1")
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(골_날짜수가_1_미만인_골_생성_dto))
        ).andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 골_아이디로_조회하면_해당_골의_정보를_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.readGoalById(유효한_골_아이디)).willReturn(유효한_골_dto);

        // when & then
        mockMvc.perform(get("/goals/{goalId}", 유효한_골_아이디)
                .header("X-API-VERSION", "1")
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.id", is(유효한_골_dto.id()), Long.class),
                jsonPath("$.name", is(유효한_골_dto.name()), String.class),
                jsonPath("$.memo", is(유효한_골_dto.memo()), String.class),
                jsonPath("$.startDate", is(유효한_골_dto.startDate().toString()), String.class),
                jsonPath("$.endDate", is(유효한_골_dto.endDate().toString()), String.class),
                jsonPath("$.days", is(유효한_골_dto.days()), long.class),
                jsonPath("$.managerId", is(유효한_골_dto.managerId()), Long.class),
                jsonPath("$.teamUserIds.[0]", is(유효한_골_dto.teamUserIds().get(0)), Long.class)
        ).andDo(print()).andDo(restDocs.document(
                pathParameters(parameterWithName("goalId").description("조회할 골 아이디")),
                requestHeaders(
                        headerWithName("X-API-VERSION").description("요청 버전"),
                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("골 아이디"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("골 제목"),
                        fieldWithPath("memo").type(JsonFieldType.STRING).description("골 메모"),
                        fieldWithPath("startDate").type(JsonFieldType.STRING).description("골 시작날짜"),
                        fieldWithPath("endDate").type(JsonFieldType.STRING).description("골 종료날짜"),
                        fieldWithPath("days").type(JsonFieldType.NUMBER).description("골 날짜 수"),
                        fieldWithPath("managerId").type(JsonFieldType.NUMBER).description("골 관리자 아이디"),
                        fieldWithPath("teamUserIds").type(JsonFieldType.ARRAY).description("골 팀 사용자 아이디")
                )
        ));
    }
}
