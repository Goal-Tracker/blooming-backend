package com.backend.blooming.stamp.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedThreadLocal;
import com.backend.blooming.common.RestDocsConfiguration;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.stamp.application.StampService;
import com.backend.blooming.stamp.application.exception.ForbiddenStampToCreateException;
import com.backend.blooming.stamp.application.exception.ForbiddenStampToReadException;
import com.backend.blooming.stamp.domain.exception.InvalidStampException;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StampController.class)
@AutoConfigureRestDocs
@Import({RestDocsConfiguration.class, AuthenticatedThreadLocal.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StampControllerTest extends StampControllerTestFixture {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StampService stampService;

    @Autowired
    private RestDocumentationResultHandler restDocs;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private UserRepository userRepository;

    @Test
    void 스탬프_생성을_요청하면_새로운_스탬프를_생성한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(stampService.createStamp(유효한_스탬프_생성_dto)).willReturn(추가한_스탬프_dto);

        // when & then
        mockMvc.perform(multipart("/goals/{goalId}/stamp", 유효한_골_아이디, HttpMethod.POST)
                .file(스탬프_추가_요청)
                .file(추가할_스탬프_이미지)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.id", is(유효한_스탬프_응답_dto.id()), Long.class),
                jsonPath("$.userName", is(유효한_스탬프_응답_dto.userName())),
                jsonPath("$.userColor", is(유효한_스탬프_응답_dto.userColor())),
                jsonPath("$.day", is(유효한_스탬프_응답_dto.day()), long.class),
                jsonPath("$.message", is(유효한_스탬프_응답_dto.message())),
                jsonPath("$.stampImageUrl", is(유효한_스탬프_응답_dto.stampImageUrl()))
        ).andDo(print()).andDo(restDocs.document(
                pathParameters(
                        parameterWithName("goalId").description("스탬프를 생성할 골 아이디")
                ),
                requestHeaders(
                        headerWithName("X-API-VERSION").description("요청 버전"),
                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                ),
                requestParts(
                        partWithName("request").description("스탬프 정보"),
                        partWithName("stampImage").description("스탬프 이미지")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("스탬프 아이디"),
                        fieldWithPath("userName").type(JsonFieldType.STRING).description("스탬프를 추가한 사용자 이름"),
                        fieldWithPath("userColor").type(JsonFieldType.STRING).description("스탬프를 추가한 사용자 테마 색상코드"),
                        fieldWithPath("day").type(JsonFieldType.NUMBER).description("스탬프 날짜"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("스탬프 메시지"),
                        fieldWithPath("stampImageUrl").type(JsonFieldType.STRING).description("스탬프 이미지 url")
                )
        ));
    }

    @Test
    void 존재하지_않는_골에서_스탬프_생성_요청할_경우_404_예외를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(stampService.createStamp(존재하지_않는_골에서_생성한_스탬프_dto)).willThrow(new NotFoundGoalException());

        // when & then
        mockMvc.perform(multipart("/goals/{goalId}/stamp", 존재하지_않는_골_아이디, HttpMethod.POST)
                .file(존재하지_않는_골의_스탬프_추가_요청)
                .file(추가할_스탬프_이미지)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 스탬프_생성_요청한_사용자가_골_참여자가_아닌_경우_403_예외를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(stampService.createStamp(권한이_없는_사용자가_생성한_스탬프_dto)).willThrow(new ForbiddenStampToCreateException());

        // when & then
        mockMvc.perform(multipart("/goals/{goalId}/stamp", 유효한_골_아이디, HttpMethod.POST)
                .file(권한이_없는_사용자의_스탬프_추가_요청)
                .file(추가할_스탬프_이미지)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 생성_요청한_날짜의_스탬가_이미_존재할_경우_400_예외를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(stampService.createStamp(이미_존재하는_스탬프_생성_dto))
                .willThrow(new InvalidStampException.InvalidStampToCreate());

        // when & then
        mockMvc.perform(multipart("/goals/{goalId}/stamp", 유효한_골_아이디, HttpMethod.POST)
                .file(이미_존재하는_스탬프_추가_요청)
                .file(추가할_스탬프_이미지)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 요청한_골_아이디에_대한_모든_스탬프를_조회한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(stampService.readAllByGoalId(유효한_골_아이디, 사용자_토큰_정보.userId())).willReturn(유효한_스탬프_목록_dto);

        // when & then
        mockMvc.perform(get("/goals/{goalId}/stamps", 유효한_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.stamps.1.[0].userId", is(유효한_스탬프_목록_응답.stamps().get(1L).get(0).userId()), Long.class),
                jsonPath("$.stamps.1.[0].name", is(유효한_스탬프_목록_응답.stamps().get(1L).get(0).name()), String.class),
                jsonPath("$.stamps.1.[0].color", is(유효한_스탬프_목록_응답.stamps().get(1L).get(0).color()), String.class),
                jsonPath("$.stamps.1.[0].message", is(유효한_스탬프_목록_응답.stamps().get(1L).get(0).message()), String.class),
                jsonPath("$.stamps.1.[0].day", is(유효한_스탬프_목록_응답.stamps().get(1L).get(0).day()), long.class),
                jsonPath("$.stamps.1.[0].stampImageUrl", is(유효한_스탬프_목록_응답.stamps().get(1L).get(0).stampImageUrl()), String.class),
                jsonPath("$.stamps.2.[0].userId", is(유효한_스탬프_목록_응답.stamps().get(2L).get(0).userId()), Long.class),
                jsonPath("$.stamps.2.[1].userId", is(유효한_스탬프_목록_응답.stamps().get(2L).get(1).userId()), Long.class)
        ).andDo(print()).andDo(restDocs.document(
                pathParameters(parameterWithName("goalId").description("스탬프를 조회할 골 아이디")),
                requestHeaders(
                        headerWithName("X-API-VERSION").description("요청 버전"),
                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                ),
                responseFields(
                        fieldWithPath("stamps.*.[].userId").type(JsonFieldType.NUMBER).description("스탬프를 찍은 사용자 아이디"),
                        fieldWithPath("stamps.*.[].name").type(JsonFieldType.STRING).description("스탬프를 찍은 사용자 이름"),
                        fieldWithPath("stamps.*.[].color").type(JsonFieldType.STRING).description("스탬프를 찍은 사용자 프로필 색상코드"),
                        fieldWithPath("stamps.*.[].message").type(JsonFieldType.STRING).description("스탬프를 찍은 사용자 프로필 색상코드"),
                        fieldWithPath("stamps.*.[].day").type(JsonFieldType.NUMBER).description("스탬프를 찍은 날짜"),
                        fieldWithPath("stamps.*.[].stampImageUrl").type(JsonFieldType.STRING).description("스탬프 이미지 주소")
                )
        ));
    }

    @Test
    void 스탬프_조회시_존재하지_않는_골의_스탬프를_조회할_경우_404_예외를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        given(stampService.readAllByGoalId(존재하지_않는_골_아이디, 사용자_토큰_정보.userId()))
                .willThrow(new NotFoundGoalException());

        // when & then
        mockMvc.perform(get("/goals/{goalId}/stamps", 존재하지_않는_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 골_참여자가_아닌_사용자가_스탬프_조회를_요청한_경우_403_예외를_발생한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(골_참여자가_아닌_사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(골_참여자가_아닌_사용자_토큰_정보.userId())).willReturn(true);
        given(stampService.readAllByGoalId(유효한_골_아이디, 골_참여자가_아닌_사용자_아이디))
                .willThrow(new ForbiddenStampToReadException());

        // when & then
        mockMvc.perform(get("/goals/{goalId}/stamps", 유효한_골_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }
}
