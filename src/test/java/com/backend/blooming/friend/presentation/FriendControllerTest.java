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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

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
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(friendService.request(사용자_아이디, 친구_요청을_받은_사용자_아이디)).willReturn(친구_요청_아이디);

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
    void 친구_요청을_보낸_사용자_목록을_조회한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(friendService.readAllByRequestId(사용자_아이디)).willReturn(친구_요청을_보낸_사용자들_정보_dto);

        // when & then
        mockMvc.perform(get("/friends/request")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.friends.[0].id", is(친구_요청_정보_dto1.id()), Long.class),
                jsonPath("$.friends.[0].friend.id", is(친구_요청_정보_dto1.friend().id()), Long.class),
                jsonPath("$.friends.[0].friend.email", is(친구_요청_정보_dto1.friend().email())),
                jsonPath("$.friends.[0].friend.name", is(친구_요청_정보_dto1.friend().name())),
                jsonPath("$.friends.[0].friend.color", is(친구_요청_정보_dto1.friend().color())),
                jsonPath("$.friends.[0].friend.statusMessage", is(친구_요청_정보_dto1.friend().statusMessage())),
                jsonPath("$.friends.[0].isFriends", is(친구_요청_정보_dto1.isFriends()), Boolean.class),
                jsonPath("$.friends.[1].id", is(친구_요청_정보_dto2.id()), Long.class),
                jsonPath("$.friends.[1].friend.id", is(친구_요청_정보_dto2.friend().id()), Long.class),
                jsonPath("$.friends.[1].friend.email", is(친구_요청_정보_dto2.friend().email())),
                jsonPath("$.friends.[1].friend.name", is(친구_요청_정보_dto2.friend().name())),
                jsonPath("$.friends.[1].friend.color", is(친구_요청_정보_dto2.friend().color())),
                jsonPath("$.friends.[1].friend.statusMessage", is(친구_요청_정보_dto2.friend().statusMessage())),
                jsonPath("$.friends.[1].isFriends", is(친구_요청_정보_dto2.isFriends()), Boolean.class)
        ).andDo(print()).andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("X-API-VERSION").description("요청 버전"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                        ),
                        responseFields(
                                // TODO: 12/14/23 [고민] description의 경우 개행을 할까요 말까요? 개인적으로는 개행하지 않은게 더 보기 좋은 것 같은데 의견 주시면 감사하겠습니다!
                                fieldWithPath("friends").type(JsonFieldType.ARRAY).description("친구 요청을 보낸 사용자 목록"),
                                fieldWithPath("friends.[].id").type(JsonFieldType.NUMBER).description("친구 요청 아이디"),
                                fieldWithPath("friends.[].friend.id").type(JsonFieldType.NUMBER).description("사용자 아이디"),
                                fieldWithPath("friends.[].friend.email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                fieldWithPath("friends.[].friend.name").type(JsonFieldType.STRING).description("사용자 이름"),
                                fieldWithPath("friends.[].friend.color").type(JsonFieldType.STRING).description("사용자 테마 색상"),
                                fieldWithPath("friends.[].friend.statusMessage").type(JsonFieldType.STRING).description("사용자 상태 메시지"),
                                fieldWithPath("friends.[].isFriends").type(JsonFieldType.BOOLEAN).description("친구 여부")
                        )
                )
        );
    }

    @Test
    void 친구_요청을_받은_사용자_목록을_조회한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(friendService.readAllByRequestedId(사용자_아이디)).willReturn(친구_요청을_받은_사용자들_정보_dto);

        // when & then
        mockMvc.perform(get("/friends/requested")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.friends.[0].id", is(받은_친구_요청_정보_dto1.id()), Long.class),
                jsonPath("$.friends.[0].friend.id", is(받은_친구_요청_정보_dto1.friend().id()), Long.class),
                jsonPath("$.friends.[0].friend.email", is(받은_친구_요청_정보_dto1.friend().email())),
                jsonPath("$.friends.[0].friend.name", is(받은_친구_요청_정보_dto1.friend().name())),
                jsonPath("$.friends.[0].friend.color", is(받은_친구_요청_정보_dto1.friend().color())),
                jsonPath("$.friends.[0].friend.statusMessage", is(받은_친구_요청_정보_dto1.friend().statusMessage())),
                jsonPath("$.friends.[0].isFriends", is(받은_친구_요청_정보_dto1.isFriends()), Boolean.class),
                jsonPath("$.friends.[1].id", is(받은_친구_요청_정보_dto2.id()), Long.class),
                jsonPath("$.friends.[1].friend.id", is(받은_친구_요청_정보_dto2.friend().id()), Long.class),
                jsonPath("$.friends.[1].friend.email", is(받은_친구_요청_정보_dto2.friend().email())),
                jsonPath("$.friends.[1].friend.name", is(받은_친구_요청_정보_dto2.friend().name())),
                jsonPath("$.friends.[1].friend.color", is(받은_친구_요청_정보_dto2.friend().color())),
                jsonPath("$.friends.[1].friend.statusMessage", is(받은_친구_요청_정보_dto2.friend().statusMessage())),
                jsonPath("$.friends.[1].isFriends", is(받은_친구_요청_정보_dto2.isFriends()), Boolean.class)
        ).andDo(print()).andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("X-API-VERSION").description("요청 버전"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("friends").type(JsonFieldType.ARRAY).description("친구 요청을 받은 사용자 목록"),
                                fieldWithPath("friends.[].id").type(JsonFieldType.NUMBER).description("친구 요청 아이디"),
                                fieldWithPath("friends.[].friend.id").type(JsonFieldType.NUMBER).description("사용자 아이디"),
                                fieldWithPath("friends.[].friend.email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                fieldWithPath("friends.[].friend.name").type(JsonFieldType.STRING).description("사용자 이름"),
                                fieldWithPath("friends.[].friend.color").type(JsonFieldType.STRING).description("사용자 테마 색상"),
                                fieldWithPath("friends.[].friend.statusMessage").type(JsonFieldType.STRING).description("사용자 상태 메시지"),
                                fieldWithPath("friends.[].isFriends").type(JsonFieldType.BOOLEAN).description("친구 여부")
                        )
                )
        );
    }

    @Test
    void 서로_친구인_사용자_목록을_조회한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(friendService.readAllMutualByUserId(사용자_아이디)).willReturn(서로_친구인_사용자들_정보_dto);

        // when & then
        mockMvc.perform(get("/friends/mutual")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.friends.[0].id", is(서로_친구인_친구_요청_정보_dto1.id()), Long.class),
                jsonPath("$.friends.[0].friend.id", is(서로_친구인_친구_요청_정보_dto1.friend().id()), Long.class),
                jsonPath("$.friends.[0].friend.email", is(서로_친구인_친구_요청_정보_dto1.friend().email())),
                jsonPath("$.friends.[0].friend.name", is(서로_친구인_친구_요청_정보_dto1.friend().name())),
                jsonPath("$.friends.[0].friend.color", is(서로_친구인_친구_요청_정보_dto1.friend().color())),
                jsonPath("$.friends.[0].friend.statusMessage", is(서로_친구인_친구_요청_정보_dto1.friend().statusMessage())),
                jsonPath("$.friends.[0].isFriends", is(서로_친구인_친구_요청_정보_dto1.isFriends()), Boolean.class),
                jsonPath("$.friends.[1].id", is(서로_친구인_친구_요청_정보_dto2.id()), Long.class),
                jsonPath("$.friends.[1].friend.id", is(서로_친구인_친구_요청_정보_dto2.friend().id()), Long.class),
                jsonPath("$.friends.[1].friend.email", is(서로_친구인_친구_요청_정보_dto2.friend().email())),
                jsonPath("$.friends.[1].friend.name", is(서로_친구인_친구_요청_정보_dto2.friend().name())),
                jsonPath("$.friends.[1].friend.color", is(서로_친구인_친구_요청_정보_dto2.friend().color())),
                jsonPath("$.friends.[1].friend.statusMessage", is(서로_친구인_친구_요청_정보_dto2.friend().statusMessage())),
                jsonPath("$.friends.[1].isFriends", is(서로_친구인_친구_요청_정보_dto2.isFriends()), Boolean.class)
        ).andDo(print()).andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("X-API-VERSION").description("요청 버전"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("friends").type(JsonFieldType.ARRAY).description("서로 친구인 사용자 목록"),
                                fieldWithPath("friends.[].id").type(JsonFieldType.NUMBER).description("친구 요청 아이디"),
                                fieldWithPath("friends.[].friend.id").type(JsonFieldType.NUMBER).description("사용자 아이디"),
                                fieldWithPath("friends.[].friend.email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                fieldWithPath("friends.[].friend.name").type(JsonFieldType.STRING).description("사용자 이름"),
                                fieldWithPath("friends.[].friend.color").type(JsonFieldType.STRING).description("사용자 테마 색상"),
                                fieldWithPath("friends.[].friend.statusMessage").type(JsonFieldType.STRING).description("사용자 상태 메시지"),
                                fieldWithPath("friends.[].isFriends").type(JsonFieldType.BOOLEAN).description("친구 여부")
                        )
                )
        );
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
