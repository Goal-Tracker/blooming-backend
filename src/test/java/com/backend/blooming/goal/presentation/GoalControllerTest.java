package com.backend.blooming.goal.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedThreadLocal;
import com.backend.blooming.common.RestDocsConfiguration;
import com.backend.blooming.goal.application.GoalService;
import com.backend.blooming.goal.application.exception.DeleteGoalForbiddenException;
import com.backend.blooming.goal.application.exception.ForbiddenGoalToReadException;
import com.backend.blooming.goal.application.exception.InvalidGoalAcceptException;
import com.backend.blooming.goal.application.exception.InvalidGoalException;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.goal.application.exception.UpdateGoalForbiddenException;
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

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
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
    void 골_생성을_요청하면_새로운_골을_생성한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.createGoal(유효한_골_생성_dto)).willReturn(유효한_골_아이디);

        // when & then
        mockMvc.perform(post("/goals")
                .header("X-API-VERSION", 1)
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
    void 골_생성시_관리자와_친구가_아닌_사용자가_참여자로_있는_경우_400_예외를_발생시킨다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.createGoal(친구가_아닌_사용자가_참여자로_있는_골_생성_dto))
                .willThrow(new InvalidGoalException.InvalidInvalidUserToParticipate());

        // when & then
        mockMvc.perform(post("/goals")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(친구가_아닌_사용자가_참여자로_있는_골_생성_dto))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 골_종료날짜가_시작날짜보다_이전인_경우_400_예외를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.createGoal(골_종료날짜가_시작날짜보다_이전인_골_생성_dto))
                .willThrow(new InvalidGoalException.InvalidInvalidGoalPeriod());

        // when & then
        mockMvc.perform(post("/goals")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(골_종료날짜가_시작날짜보다_이전인_골_생성_dto))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 골_날짜가_100_초과인_경우_400_예외를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.createGoal(골_날짜수가_100_초과인_골_생성_dto))
                .willThrow(new InvalidGoalException.InvalidInvalidGoalDays());

        // when & then
        mockMvc.perform(post("/goals")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(골_날짜수가_100_초과인_골_생성_dto))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 골_생성시_사용자_리스트가_5명_초과인_경우_400_예외를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.createGoal(참여자_리스트가_5명_초과인_골_생성_dto))
                .willThrow(new InvalidGoalException.InvalidInvalidUsersSize());

        // when & then
        mockMvc.perform(post("/goals")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(참여자_리스트가_5명_초과인_골_생성_dto))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 골_아이디로_조회하면_해당_골의_정보를_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.readGoalDetailById(유효한_골_아이디, 사용자_토큰_정보.userId())).willReturn(유효한_골_dto);

        // when & then
        mockMvc.perform(get("/goals/{goalId}", 유효한_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.id", is(유효한_골_응답_dto.id()), Long.class),
                jsonPath("$.name", is(유효한_골_응답_dto.name()), String.class),
                jsonPath("$.memo", is(유효한_골_응답_dto.memo()), String.class),
                jsonPath("$.startDate", is(유효한_골_응답_dto.startDate().toString()), String.class),
                jsonPath("$.endDate", is(유효한_골_응답_dto.endDate().toString()), String.class),
                jsonPath("$.days", is(유효한_골_응답_dto.days()), long.class),
                jsonPath("$.managerId", is(유효한_골_응답_dto.managerId()), Long.class),
                jsonPath("$.teams.[0].id", is(유효한_골_응답_dto.teams().get(0).id()), Long.class),
                jsonPath("$.teams.[0].name", is(유효한_골_응답_dto.teams().get(0).name()), String.class),
                jsonPath("$.teams.[0].colorCode", is(유효한_골_응답_dto.teams().get(0).colorCode()), String.class),
                jsonPath("$.teams.[0].statusMessage", is(유효한_골_응답_dto.teams().get(0).statusMessage()), String.class),
                jsonPath("$.teams.[1].id", is(유효한_골_응답_dto.teams().get(1).id()), Long.class)
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
                        fieldWithPath("teams.[].id").type(JsonFieldType.NUMBER).description("골 참여자 아이디"),
                        fieldWithPath("teams.[].name").type(JsonFieldType.STRING).description("골 참여자 이름"),
                        fieldWithPath("teams.[].colorCode").type(JsonFieldType.STRING).description("골 참여자 색상"),
                        fieldWithPath("teams.[].statusMessage").type(JsonFieldType.STRING).description("골 참여자 상태메시지")
                )
        ));
    }

    @Test
    void 존재하지_않는_골을_조회했을_때_404_예외를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.readGoalDetailById(유효한_골_아이디, 사용자_토큰_정보.userId())).willThrow(new NotFoundGoalException());

        // when & then
        mockMvc.perform(get("/goals/{goalId}", 유효한_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 골_참여자가_아닌_사용자_또는_골_초대를_수락하지_않은_사용자가_조회한_경우_403_예외를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.readGoalDetailById(유효한_골_아이디, 사용자_토큰_정보.userId()))
                .willThrow(new ForbiddenGoalToReadException());

        // when & then
        mockMvc.perform(get("/goals/{goalId}", 유효한_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 현재_로그인한_사용자가_참여한_현재_진행중인_모든_골을_조회한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.readAllGoalByUserIdAndInProgress(사용자_토큰_정보.userId(), LocalDate.now()))
                .willReturn(사용자가_참여한_현재_진행중인_골_목록_dto);

        // when & then
        mockMvc.perform(get("/goals/all/progress")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.goals.[0].id", is(사용자가_참여한_현재_진행중인_골_목록_응답_dto.goals().get(0).id()), Long.class),
                jsonPath("$.goals.[0].name", is(사용자가_참여한_현재_진행중인_골_목록_응답_dto.goals().get(0).name()), String.class)
        ).andDo(print()).andDo(restDocs.document(
                requestHeaders(
                        headerWithName("X-API-VERSION").description("요청 버전"),
                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                ),
                responseFields(
                        fieldWithPath("goals.[].id").type(JsonFieldType.NUMBER).description("골 아이디"),
                        fieldWithPath("goals.[].name").type(JsonFieldType.STRING).description("골 제목"),
                        fieldWithPath("goals.[].startDate").type(JsonFieldType.STRING).description("골 시작날짜"),
                        fieldWithPath("goals.[].endDate").type(JsonFieldType.STRING).description("골 종료날짜"),
                        fieldWithPath("goals.[].days").type(JsonFieldType.NUMBER).description("골 날짜 수"),
                        fieldWithPath("goals.[].teams.[].id").type(JsonFieldType.NUMBER).description("골 참여자 아이디"),
                        fieldWithPath("goals.[].teams.[].name").type(JsonFieldType.STRING).description("골 참여자 이름"),
                        fieldWithPath("goals.[].teams.[].colorCode").type(JsonFieldType.STRING).description("골 참여자 색상")
                )
        ));
    }

    @Test
    void 현재_로그인한_사용자가_참여한_종료된_모든_골을_조회한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.readAllGoalByUserIdAndFinished(사용자_토큰_정보.userId(), LocalDate.now()))
                .willReturn(사용자가_참여한_종료된_골_목록_dto);

        // when & then
        mockMvc.perform(get("/goals/all/finished")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.goals.[0].id", is(사용자가_참여한_종료된_골_목록_응답_dto.goals().get(0).id()), Long.class),
                jsonPath("$.goals.[0].name", is(사용자가_참여한_종료된_골_목록_응답_dto.goals().get(0).name()), String.class)
        ).andDo(print()).andDo(restDocs.document(
                requestHeaders(
                        headerWithName("X-API-VERSION").description("요청 버전"),
                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                ),
                responseFields(
                        fieldWithPath("goals.[].id").type(JsonFieldType.NUMBER).description("골 아이디"),
                        fieldWithPath("goals.[].name").type(JsonFieldType.STRING).description("골 제목"),
                        fieldWithPath("goals.[].startDate").type(JsonFieldType.STRING).description("골 시작날짜"),
                        fieldWithPath("goals.[].endDate").type(JsonFieldType.STRING).description("골 종료날짜"),
                        fieldWithPath("goals.[].days").type(JsonFieldType.NUMBER).description("골 날짜 수"),
                        fieldWithPath("goals.[].teams.[].id").type(JsonFieldType.NUMBER).description("골 참여자 아이디"),
                        fieldWithPath("goals.[].teams.[].name").type(JsonFieldType.STRING).description("골 참여자 이름"),
                        fieldWithPath("goals.[].teams.[].colorCode").type(JsonFieldType.STRING).description("골 참여자 색상")
                )
        ));
    }

    @Test
    void 요청한_아이디의_골을_삭제한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        willDoNothing().given(goalService).delete(유효한_골_아이디, 사용자_토큰_정보.userId());

        // when & then
        mockMvc.perform(delete("/goals/{goalId}", 유효한_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isNoContent()
        ).andDo(print()).andDo(restDocs.document(
                pathParameters(parameterWithName("goalId").description("조회할 골 아이디")),
                requestHeaders(
                        headerWithName("X-API-VERSION").description("요청 버전"),
                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                )
        ));
    }

    @Test
    void 삭제_요청한_아이디의_골이_존재하지_않는_경우_404_에러를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        willThrow(new NotFoundGoalException()).given(goalService).delete(유효한_골_아이디, 사용자_토큰_정보.userId());

        // when & then
        mockMvc.perform(delete("/goals/{goalId}", 유효한_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 현재_로그인한_사용자가_요청한_아이디의_골을_삭제할_권한이_없는_경우_403_에러를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        willThrow(new DeleteGoalForbiddenException()).given(goalService).delete(유효한_골_아이디, 사용자_토큰_정보.userId());

        // when & then
        mockMvc.perform(delete("/goals/{goalId}", 유효한_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 요청한_내용으로_골을_수정한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.update(사용자_토큰_정보.userId(), 유효한_골_아이디, 수정_요청한_골_dto)).willReturn(수정_후_골_dto);

        // when & then
        mockMvc.perform(patch("/goals/{goalId}", 유효한_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(수정_요청한_골_dto))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.id", is(수정_후_골_응답_dto.id()), Long.class),
                jsonPath("$.name", is(수정_후_골_응답_dto.name()), String.class),
                jsonPath("$.memo", is(수정_후_골_응답_dto.memo()), String.class),
                jsonPath("$.startDate", is(수정_후_골_응답_dto.startDate().toString()), String.class),
                jsonPath("$.endDate", is(수정_후_골_응답_dto.endDate().toString()), String.class),
                jsonPath("$.days", is(수정_후_골_응답_dto.days()), long.class),
                jsonPath("$.managerId", is(수정_후_골_응답_dto.managerId()), Long.class),
                jsonPath("$.teams.[0].id", is(수정_후_골_응답_dto.teams().get(0).id()), Long.class),
                jsonPath("$.teams.[0].name", is(수정_후_골_응답_dto.teams().get(0).name()), String.class),
                jsonPath("$.teams.[0].colorCode", is(수정_후_골_응답_dto.teams().get(0).colorCode()), String.class),
                jsonPath("$.teams.[0].statusMessage", is(수정_후_골_응답_dto.teams().get(0).statusMessage()), String.class),
                jsonPath("$.teams.[1].id", is(수정_후_골_응답_dto.teams().get(1).id()), Long.class),
                jsonPath("$.teams.[2].id", is(수정_후_골_응답_dto.teams().get(2).id()), Long.class)
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
                        fieldWithPath("teams.[].id").type(JsonFieldType.NUMBER).description("골 참여자 아이디"),
                        fieldWithPath("teams.[].name").type(JsonFieldType.STRING).description("골 참여자 이름"),
                        fieldWithPath("teams.[].colorCode").type(JsonFieldType.STRING).description("골 참여자 색상"),
                        fieldWithPath("teams.[].statusMessage").type(JsonFieldType.STRING).description("골 참여자 상태메시지")
                )
        ));
    }

    @Test
    void 수정_요청한_골이_존재하지_않는_경우_404_에러를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.update(사용자_토큰_정보.userId(), 존재하지_않는_골_아이디, 수정_요청한_골_dto))
                .willThrow(new NotFoundGoalException());

        // when & then
        mockMvc.perform(patch("/goals/{goalId}", 존재하지_않는_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(수정_요청한_골_dto))
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 현재_로그인한_사용자가_요청한_아이디의_골을_수정할_권한이_없는_경우_403_에러를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.update(사용자_토큰_정보.userId(), 유효한_골_아이디, 수정_요청한_골_dto))
                .willThrow(new UpdateGoalForbiddenException.ForbiddenUserToUpdate());

        // when & then
        mockMvc.perform(patch("/goals/{goalId}", 유효한_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(수정_요청한_골_dto))
        ).andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 수정_요청한_골의_종료날짜가_기존_종료날짜보다_이전인_경우_400_에러를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.update(사용자_토큰_정보.userId(), 유효한_골_아이디, 수정_요청한_골_dto))
                .willThrow(new InvalidGoalException.InvalidInvalidUpdateEndDate());

        // when & then
        mockMvc.perform(patch("/goals/{goalId}", 유효한_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(수정_요청한_골_dto))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 수정_요청한_골_종료날짜가_null인_경우_수정을_하지_않는다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.update(사용자_토큰_정보.userId(), 유효한_골_아이디, 골_종료날짜가_null인_골_dto))
                .willReturn(골_종료날짜가_null인_수정_후_골_dto);

        // when & then
        mockMvc.perform(patch("/goals/{goalId}", 유효한_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(골_종료날짜가_null인_수정_요청_골_dto))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.id", is(수정_후_골_응답_dto.id()), Long.class),
                jsonPath("$.name", is(수정_후_골_응답_dto.name()), String.class),
                jsonPath("$.memo", is(수정_후_골_응답_dto.memo()), String.class),
                jsonPath("$.startDate", is(수정_후_골_응답_dto.startDate().toString()), String.class),
                jsonPath("$.endDate", is(골_종료날짜가_null인_수정_후_골_dto.endDate().toString()), String.class),
                jsonPath("$.days", is(골_종료날짜가_null인_수정_후_골_dto.days()), long.class),
                jsonPath("$.managerId", is(수정_후_골_응답_dto.managerId()), Long.class),
                jsonPath("$.teams.[0].id", is(수정_후_골_응답_dto.teams().get(0).id()), Long.class),
                jsonPath("$.teams.[1].id", is(수정_후_골_응답_dto.teams().get(1).id()), Long.class),
                jsonPath("$.teams.[2].id", is(수정_후_골_응답_dto.teams().get(2).id()), Long.class)
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
                        fieldWithPath("teams.[].id").type(JsonFieldType.NUMBER).description("골 참여자 아이디"),
                        fieldWithPath("teams.[].name").type(JsonFieldType.STRING).description("골 참여자 이름"),
                        fieldWithPath("teams.[].colorCode").type(JsonFieldType.STRING).description("골 참여자 색상"),
                        fieldWithPath("teams.[].statusMessage").type(JsonFieldType.STRING).description("골 참여자 상태메시지")
                )
        ));
    }

    @Test
    void 수정_요청한_골_참여자_목록이_null인_경우_수정을_하지_않는다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(goalService.update(사용자_토큰_정보.userId(), 유효한_골_아이디, 골_참여자_목록이_null인_골_dto))
                .willReturn(골_참여자가_null인_수정_후_골_dto);

        // when & then
        mockMvc.perform(patch("/goals/{goalId}", 유효한_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(골_참여자_목록이_null인_수정_요청_골_dto))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.id", is(수정_후_골_응답_dto.id()), Long.class),
                jsonPath("$.name", is(수정_후_골_응답_dto.name()), String.class),
                jsonPath("$.memo", is(수정_후_골_응답_dto.memo()), String.class),
                jsonPath("$.startDate", is(수정_후_골_응답_dto.startDate().toString()), String.class),
                jsonPath("$.endDate", is(수정_후_골_응답_dto.endDate().toString()), String.class),
                jsonPath("$.days", is(수정_후_골_응답_dto.days()), long.class),
                jsonPath("$.managerId", is(수정_후_골_응답_dto.managerId()), Long.class),
                jsonPath("$.teams.[0].id", is(수정_후_골_응답_dto.teams().get(0).id()), Long.class),
                jsonPath("$.teams.[1].id", is(수정_후_골_응답_dto.teams().get(1).id()), Long.class)
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
                        fieldWithPath("teams.[].id").type(JsonFieldType.NUMBER).description("골 참여자 아이디"),
                        fieldWithPath("teams.[].name").type(JsonFieldType.STRING).description("골 참여자 이름"),
                        fieldWithPath("teams.[].colorCode").type(JsonFieldType.STRING).description("골 참여자 색상"),
                        fieldWithPath("teams.[].statusMessage").type(JsonFieldType.STRING).description("골 참여자 상태메시지")
                )
        ));
    }

    @Test
    void 골_초대를_수락한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        willDoNothing().given(goalService).acceptGoalRequest(사용자_토큰_정보.userId(), 유효한_골_아이디);

        // when & then
        mockMvc.perform(post("/goals/{goalId}/accept", 유효한_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isNoContent()
        ).andDo(print()).andDo(restDocs.document(
                pathParameters(parameterWithName("goalId").description("조회할 골 아이디")),
                requestHeaders(
                        headerWithName("X-API-VERSION").description("요청 버전"),
                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                )
        ));
    }

    @Test
    void 골에_초대되지_않은_사람이_골_수락을_요청한_경우_400_에러를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        willThrow(new InvalidGoalAcceptException.InvalidInvalidUserToAcceptGoal())
                .given(goalService)
                .acceptGoalRequest(사용자_토큰_정보.userId(), 유효한_골_아이디);

        // when & then
        mockMvc.perform(post("/goals/{goalId}/accept", 유효한_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 골_관리자가_골_수락을_요청한_경우_400_에러를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        willThrow(new InvalidGoalAcceptException.InvalidInvalidGoalAcceptByManager())
                .given(goalService)
                .acceptGoalRequest(사용자_토큰_정보.userId(), 유효한_골_아이디);

        // when & then
        mockMvc.perform(post("/goals/{goalId}/accept", 유효한_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }
}
