package com.backend.blooming.stamp.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedThreadLocal;
import com.backend.blooming.common.RestDocsConfiguration;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.stamp.application.StampService;
import com.backend.blooming.stamp.application.exception.CreateStampForbiddenException;
import com.backend.blooming.stamp.domain.exception.InvalidStampException;
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

    @Autowired
    private ObjectMapper objectMapper;

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
        given(stampService.createStamp(유효한_스탬프_생성_dto)).willReturn(유효한_스탬프_아이디);

        // when & then
        mockMvc.perform(post("/stamps")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(유효한_스탬프_생성_요청_dto))
        ).andExpectAll(
                status().isNoContent()
        ).andDo(print()).andDo(restDocs.document(
                requestHeaders(
                        headerWithName("X-API-VERSION").description("요청 버전"),
                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                ),
                requestFields(
                        fieldWithPath("goalId").type(JsonFieldType.NUMBER).description("골 아이디"),
                        fieldWithPath("day").type(JsonFieldType.NUMBER).description("스탬프 날짜"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("스탬프 메시지")
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
        mockMvc.perform(post("/stamps")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(존재하지_않는_골에서_요청한_스탬프_생성_dto))
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
        given(stampService.createStamp(권한이_없는_사용자가_생성한_스탬프_dto)).willThrow(new CreateStampForbiddenException());

        // when & then
        mockMvc.perform(post("/stamps")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(권한이_없는_사용자가_생성_요청한_스탬프_dto))
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
        mockMvc.perform(post("/stamps")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(이미_존재하는_스탬프_생성_요청_dto))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }
}
