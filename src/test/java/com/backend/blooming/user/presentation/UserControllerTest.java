package com.backend.blooming.user.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedThreadLocal;
import com.backend.blooming.common.RestDocsConfiguration;
import com.backend.blooming.user.application.UserService;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({RestDocsConfiguration.class, AuthenticatedThreadLocal.class})
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserControllerTest extends UserControllerTestFixture {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    TokenProvider tokenProvider;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestDocumentationResultHandler restDocs;

    @Test
    void 사용자_정보를_조회한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(userService.readById(사용자_아이디)).willReturn(사용자_정보_dto);

        // when & then
        mockMvc.perform(get("/users")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.id", is(사용자_아이디), Long.class),
                jsonPath("$.oAuthId", is(사용자_정보_dto.oAuthId())),
                jsonPath("$.oAuthType", is(사용자_정보_dto.oAuthType())),
                jsonPath("$.email", is(사용자_정보_dto.email())),
                jsonPath("$.name", is(사용자_정보_dto.name())),
                jsonPath("$.color", is(사용자_정보_dto.color())),
                jsonPath("$.statusMessage", is(사용자_정보_dto.statusMessage()))
        ).andDo(restDocs.document(
                requestHeaders(
                        headerWithName("X-API-VERSION").description("요청 버전"),
                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 아이디"),
                        fieldWithPath("oAuthId").type(JsonFieldType.STRING).description("소셜 oAuth 아이디"),
                        fieldWithPath("oAuthType").type(JsonFieldType.STRING).description("소셜 타입"),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),
                        fieldWithPath("color").type(JsonFieldType.STRING).description("사용자 테마 색상 코드"),
                        fieldWithPath("statusMessage").type(JsonFieldType.STRING).description("사용자 상태 메시지")
                )
        ));
    }

    @Test
    void 사용자_정보_조회시_존재하지_않는_사용자라면_400을_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(false);

        // when & then
        mockMvc.perform(get("/users")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isUnauthorized(),
                jsonPath("$.message").exists()
        );
    }

    @Test
    void 사용자_정보_조회시_존재하지_않는_사용자라면_404를_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(userService.readById(사용자_아이디)).willThrow(new NotFoundUserException());

        // when & then
        mockMvc.perform(get("/users")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.message").exists()
        );
    }

    @Test
    void 검색한_키워드가_이름에_포한된_사용자_목록을_조회한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(userService.readAllWithKeyword(사용자_아이디, 검색어)).willReturn(검색어가_이름에_포함된_사용자들의_정보_dto);

        // when & then
        mockMvc.perform(get("/users/all")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .queryParam("keyword", 검색어)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.users.[0].id", is(사용자_정보_dto1.id()), Long.class),
                jsonPath("$.users.[0].email", is(사용자_정보_dto1.email())),
                jsonPath("$.users.[0].name", is(사용자_정보_dto1.name())),
                jsonPath("$.users.[0].color", is(사용자_정보_dto1.color())),
                jsonPath("$.users.[0].statusMessage", is(사용자_정보_dto1.statusMessage())),
                jsonPath("$.users.[0].friendsStatus", is(사용자_정보_dto1.friendsStatus())),
                jsonPath("$.users.[1].id", is(사용자_정보_dto2.id()), Long.class),
                jsonPath("$.users.[1].email", is(사용자_정보_dto2.email())),
                jsonPath("$.users.[1].name", is(사용자_정보_dto2.name())),
                jsonPath("$.users.[1].color", is(사용자_정보_dto2.color())),
                jsonPath("$.users.[1].statusMessage", is(사용자_정보_dto2.statusMessage())),
                jsonPath("$.users.[1].friendsStatus", is(사용자_정보_dto2.friendsStatus()))
        ).andDo(restDocs.document(
                requestHeaders(
                        headerWithName("X-API-VERSION").description("요청 버전"),
                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                ),
                queryParameters(
                        parameterWithName("keyword").description("사용자 이름 검색어")
                ),
                responseFields(
                        fieldWithPath("users").type(JsonFieldType.ARRAY).description("검색된 사용자 목록"),
                        fieldWithPath("users.[].id").type(JsonFieldType.NUMBER).description("사용자 아이디"),
                        fieldWithPath("users.[].email").type(JsonFieldType.STRING).description("사용자 이메일"),
                        fieldWithPath("users.[].name").type(JsonFieldType.STRING).description("사용자 이름"),
                        fieldWithPath("users.[].color").type(JsonFieldType.STRING).description("사용자 테마 색상 코드"),
                        fieldWithPath("users.[].statusMessage").type(JsonFieldType.STRING).description("사용자 상태 메시지"),
                        fieldWithPath("users.[].friendsStatus").type(JsonFieldType.STRING)
                                                               .description("로그인한 사용자와의 친구 상태")
                )
        ));
    }

    @Test
    void 사용자의_모든_정보를_수정한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(userService.updateById(사용자_아이디, 사용자의_모든_정보_수정_dto)).willReturn(모든_정보가_수정된_사용자_정보_dto);

        // when & then
        mockMvc.perform(patch("/users")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(사용자의_모든_정보_수정_요청))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.id", is(사용자_아이디), Long.class),
                jsonPath("$.oAuthId", is(사용자_정보_dto.oAuthId())),
                jsonPath("$.oAuthType", is(사용자_정보_dto.oAuthType())),
                jsonPath("$.email", is(사용자_정보_dto.email())),
                jsonPath("$.name", is(모든_정보가_수정된_사용자_정보_dto.name())),
                jsonPath("$.color", is(모든_정보가_수정된_사용자_정보_dto.color())),
                jsonPath("$.statusMessage", is(모든_정보가_수정된_사용자_정보_dto.statusMessage()))
        ).andDo(restDocs.document(
                requestHeaders(
                        headerWithName("X-API-VERSION").description("요청 버전"),
                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("수정할 이름"),
                        fieldWithPath("color").type(JsonFieldType.STRING).description("수정할 테마 색상"),
                        fieldWithPath("statusMessage").type(JsonFieldType.STRING).description("수정할 상태 메시지")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 아이디"),
                        fieldWithPath("oAuthId").type(JsonFieldType.STRING).description("소셜 oAuth 아이디"),
                        fieldWithPath("oAuthType").type(JsonFieldType.STRING).description("소셜 타입"),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),
                        fieldWithPath("color").type(JsonFieldType.STRING).description("사용자 테마 색상"),
                        fieldWithPath("statusMessage").type(JsonFieldType.STRING).description("사용자 상태 메시지")
                )
        ));
    }

    @Test
    void 사용자의_테마_색상만_수정한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(userService.updateById(사용자_아이디, 사용자의_테마_색상만_수정_dto)).willReturn(테마_색상만_수정된_사용자_정보_dto);

        // when & then
        mockMvc.perform(patch("/users")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(사용자의_테마_색상만_수정_요청))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.id", is(사용자_아이디), Long.class),
                jsonPath("$.oAuthId", is(사용자_정보_dto.oAuthId())),
                jsonPath("$.oAuthType", is(사용자_정보_dto.oAuthType())),
                jsonPath("$.email", is(사용자_정보_dto.email())),
                jsonPath("$.name", is(사용자_정보_dto.name())),
                jsonPath("$.color", is(테마_색상만_수정된_사용자_정보_dto.color())),
                jsonPath("$.statusMessage", is(사용자_정보_dto.statusMessage()))
        );
    }

    @Test
    void 사용자의_상태_메시지만_수정한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(userService.updateById(사용자_아이디, 사용자의_상태_메시지만_수정_dto)).willReturn(상태_메시지만_수정된_사용자_정보_dto);

        // when & then
        mockMvc.perform(patch("/users")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(사용자의_상태_메시지만_수정_요청))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.id", is(사용자_아이디), Long.class),
                jsonPath("$.oAuthId", is(사용자_정보_dto.oAuthId())),
                jsonPath("$.oAuthType", is(사용자_정보_dto.oAuthType())),
                jsonPath("$.email", is(사용자_정보_dto.email())),
                jsonPath("$.name", is(사용자_정보_dto.name())),
                jsonPath("$.color", is(사용자_정보_dto.color())),
                jsonPath("$.statusMessage", is(사용자의_상태_메시지만_수정_dto.statusMessage()))
        );
    }

    @Test
    void 사용자의_이름만_수정한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(userService.updateById(사용자_아이디, 사용자의_이름만_수정_dto)).willReturn(이름만_수정된_사용자_정보_dto);

        // when & then
        mockMvc.perform(patch("/users")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(사용자의_이름만_수정_요청))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.id", is(사용자_아이디), Long.class),
                jsonPath("$.oAuthId", is(사용자_정보_dto.oAuthId())),
                jsonPath("$.oAuthType", is(사용자_정보_dto.oAuthType())),
                jsonPath("$.email", is(사용자_정보_dto.email())),
                jsonPath("$.name", is(사용자의_이름만_수정_dto.name())),
                jsonPath("$.color", is(사용자_정보_dto.color())),
                jsonPath("$.statusMessage", is(사용자_정보_dto.statusMessage()))
        );
    }


    @Test
    void 존재하지_않는_사용자_정보_수정시_400을_반환한다() throws Exception {
        // given
        given(tokenProvider.parseToken(액세스_토큰_타입, 액세스_토큰)).willReturn(사용자_토큰_정보);
        given(userRepository.existsByIdAndDeletedIsFalse(사용자_아이디)).willReturn(true);
        given(userService.updateById(사용자_아이디, 사용자의_모든_정보_수정_dto))
                .willThrow(new NotFoundUserException());

        // when & then
        mockMvc.perform(patch("/users")
                .header("X-API-VERSION", 1)
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(사용자의_모든_정보_수정_dto))
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.message").exists()
        );
    }
}
