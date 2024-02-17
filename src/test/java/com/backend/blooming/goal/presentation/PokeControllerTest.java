package com.backend.blooming.goal.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedThreadLocal;
import com.backend.blooming.common.RestDocsConfiguration;
import com.backend.blooming.goal.application.PokeService;
import com.backend.blooming.goal.application.exception.ForbiddenGoalToPokeException;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.user.application.exception.NotFoundUserException;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PokeController.class)
@AutoConfigureRestDocs
@Import({RestDocsConfiguration.class, AuthenticatedThreadLocal.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PokeControllerTest extends PokeControllerTestFixture {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokeService pokeService;

    @Autowired
    private RestDocumentationResultHandler restDocs;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private UserRepository userRepository;

    @Test
    void 특정_골의_사용자에게_콕_찌르기를_한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        willDoNothing().given(pokeService).poke(콕_찌르기_요청_dto);

        // when & then
        mockMvc.perform(post("/goals/{goalId}/poke/{userId}", 유효한_골_아이디, 콕_찌르기_수신자)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isNoContent()
        ).andDo(print()).andDo(restDocs.document(
                requestHeaders(
                        headerWithName("X-API-VERSION").description("요청 버전"),
                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                ),
                pathParameters(
                        parameterWithName("goalId").description("골 아이디"),
                        parameterWithName("userId").description("콕 찌를 사용자 아이디")
                )
        ));
    }

    @Test
    void 존재하지_않는_골에_콕_찌르기를_요청했을_때_404_예외를_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        willThrow(new NotFoundGoalException()).given(pokeService).poke(존재하지_않은_골에_콕_찌르기_요청_dto);

        // when & then
        mockMvc.perform(post("/goals/{goalId}/poke/{userId}", 존재하지_않는_골_아이디, 콕_찌르기_수신자)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 존재하지_않는_사용자가_콕_찌르기를_요청했을_때_404_예외를_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(존재하지_않는_사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(존재하지_않는_사용자_토큰_정보.userId())).willReturn(true);
        willThrow(new NotFoundUserException()).given(pokeService).poke(존재하지_않은_사용자가_콕_찌르기_요청_dto);

        // when & then
        mockMvc.perform(post("/goals/{goalId}/poke/{userId}", 유효한_골_아이디, 콕_찌르기_수신자)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 존재하지_않는_사용자에게_콕_찌르기를_요청했을_때_404_예외를_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        willThrow(new NotFoundUserException()).given(pokeService).poke(존재하지_않은_사용자에게_콕_찌르기_요청_dto);

        // when & then
        mockMvc.perform(post("/goals/{goalId}/poke/{userId}", 유효한_골_아이디, 존재하지_않는_사용자_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 팀원이_아닌_사용자가_콕_찌르기를_요청했을_때_403_예외를_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(팀원이_아닌_사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(팀원이_아닌_사용자_토큰_정보.userId())).willReturn(true);
        willThrow(new ForbiddenGoalToPokeException.SenderNotInGoalTeam()).given(pokeService)
                                                                         .poke(팀원이_아닌_사용자가_콕_찌르기_요청_dto);

        // when & then
        mockMvc.perform(post("/goals/{goalId}/poke/{userId}", 유효한_골_아이디, 콕_찌르기_수신자)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 팀원이_아닌_사용자에게_콕_찌르기를_요청했을_때_403_예외를_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_토큰_정보.userId())).willReturn(true);
        willThrow(new ForbiddenGoalToPokeException.ReceiverNotInGoalTeam()).given(pokeService)
                                                                           .poke(팀원이_아닌_사용자에게_콕_찌르기_요청_dto);

        // when & then
        mockMvc.perform(post("/goals/{goalId}/poke/{userId}", 유효한_골_아이디, 팀원이_아닌_사용자_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }
}
