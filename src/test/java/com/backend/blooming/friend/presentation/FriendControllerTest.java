package com.backend.blooming.friend.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedThreadLocal;
import com.backend.blooming.common.RestDocsConfiguration;
import com.backend.blooming.friend.application.FriendService;
import com.backend.blooming.friend.application.exception.AlreadyRequestedFriendException;
import com.backend.blooming.friend.application.exception.DeleteFriendForbiddenException;
import com.backend.blooming.friend.application.exception.FriendAcceptanceForbiddenException;
import com.backend.blooming.friend.application.exception.NotFoundFriendRequestException;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FriendController.class)
@Import({RestDocsConfiguration.class, AuthenticatedThreadLocal.class})
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FriendControllerTest extends FriendControllerTestFixture {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FriendService friendService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    TokenProvider tokenProvider;

    @Autowired
    RestDocumentationResultHandler restDocs;

    @Test
    void 친구_요청한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(친구_요청을_받은_사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(친구_요청을_받은_사용자_아이디)).willReturn(true);
        given(friendService.request(친구_요청을_받은_사용자_아이디, 친구_요청을_받은_사용자_아이디)).willReturn(친구_요청_아이디);

        // when & then
        mockMvc.perform(post("/friends/{requestedUserId}", 친구_요청을_받은_사용자_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isNoContent()
        ).andDo(print()).andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("X-API-VERSION").description("요청 버전"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                        ),
                        pathParameters(parameterWithName("requestedUserId").description("친구로 요청할 사용자 아이디"))
                )
        );
    }

    @Test
    void 존재하지_않는_사용자에게_친구_요청시_404_예외를_발생시킨다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(friendService.request(사용자_아이디, 존재하지_않는_사용자_아이다)).willThrow(new NotFoundUserException());

        // when & then
        mockMvc.perform(post("/friends/{requestedUserId}", 존재하지_않는_사용자_아이다)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 이미_친구인_사용자에게_친구_요청시_400_예외를_발생시킨다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(friendService.request(사용자_아이디, 이미_친구인_사용자_아이디)).willThrow(new AlreadyRequestedFriendException());

        // when & then
        mockMvc.perform(post("/friends/{requestedUserId}", 이미_친구인_사용자_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 친구_요청을_수락한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(친구_요청을_받은_사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(친구_요청을_받은_사용자_아이디)).willReturn(true);
        willDoNothing().given(friendService).accept(친구_요청을_받은_사용자_아이디, 친구_요청_아이디);

        // when & then
        mockMvc.perform(patch("/friends/{requestId}", 친구_요청_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isNoContent()
        ).andDo(print()).andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("X-API-VERSION").description("요청 버전"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                        ),
                        pathParameters(parameterWithName("requestId").description("친구 요청 아이디"))
                )
        );
    }

    @Test
    void 친구_요청_수락시_존재하지_않는_요청이라면_404_예외를_발생시킨다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(친구_요청을_받은_사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(친구_요청을_받은_사용자_아이디)).willReturn(true);
        willThrow(new NotFoundFriendRequestException())
                .given(friendService).accept(친구_요청을_받은_사용자_아이디, 존재하지_않는_친구_요청_아이디);

        // when & then
        mockMvc.perform(patch("/friends/{requestUserId}", 존재하지_않는_친구_요청_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 친구_요청_수락시_요청을_받은_사용자가_아니라면_403_예외를_발생시킨다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(친구_요청을_받지_않은_사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(친구_요청을_받지_않은_사용자_아이디)).willReturn(true);
        willThrow(new FriendAcceptanceForbiddenException())
                .given(friendService).accept(친구_요청을_받지_않은_사용자_아이디, 친구_요청_아이디);

        // when & then
        mockMvc.perform(patch("/friends/{requestUserId}", 친구_요청_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 친구_요청을_거절한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(친구_요청을_받은_사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(친구_요청을_받은_사용자_아이디)).willReturn(true);
        willDoNothing().given(friendService).delete(친구_요청을_받은_사용자_아이디, 친구_요청_아이디);

        // when & then
        mockMvc.perform(delete("/friends/{requestId}", 친구_요청_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isNoContent()
        ).andDo(print()).andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("X-API-VERSION").description("요청 버전"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                        ),
                        pathParameters(parameterWithName("requestId").description("친구 요청 아이디"))
                )
        );
    }

    @Test
    void 친구_요청_거절시_존재하는_요청이_아니라면_404_예외를_발생시킨다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(친구_요청을_받은_사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(친구_요청을_받은_사용자_아이디)).willReturn(true);
        willThrow(new NotFoundFriendRequestException())
                .given(friendService).delete(친구_요청을_받은_사용자_아이디, 존재하지_않는_친구_요청_아이디);

        // when & then
        mockMvc.perform(delete("/friends/{requestUserId}", 존재하지_않는_친구_요청_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }

    @Test
    void 친구_요청_거절시_요청_하거나_요청을_받은_사용자가_아니라면_403_예외를_발생시킨다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(친구_요청을_받지_않은_사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(친구_요청을_받지_않은_사용자_아이디)).willReturn(true);
        willThrow(new DeleteFriendForbiddenException())
                .given(friendService).delete(친구_요청을_받지_않은_사용자_아이디, 친구_요청_아이디);

        // when & then
        mockMvc.perform(delete("/friends/{requestUserId}", 친구_요청_아이디)
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").exists()
        ).andDo(print());
    }
}
