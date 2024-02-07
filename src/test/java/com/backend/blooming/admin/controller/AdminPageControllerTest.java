package com.backend.blooming.admin.controller;

import com.backend.blooming.admin.application.AdminPageService;
import com.backend.blooming.admin.controller.dto.UpdateFriendRequest;
import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedThreadLocal;
import com.backend.blooming.friend.application.FriendService;
import com.backend.blooming.friend.infrastructure.repository.FriendRepository;
import com.backend.blooming.goal.application.GoalService;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AdminPageController.class)
@Import(AuthenticatedThreadLocal.class)
@MockBean({TokenProvider.class, UserRepository.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminPageControllerTest extends AdminPageControllerTestFixture {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminPageService adminPageService;

    @MockBean
    private FriendService friendService;

    @MockBean
    private FriendRepository friendRepository;

    @MockBean
    private GoalService goalService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 관리자_페이지를_로드한다() throws Exception {
        // when & then
        mockMvc.perform(get("/admin"))
               .andExpectAll(
                       status().isOk(),
                       view().name("/admin/test"),
                       model().attributeExists("themes")
               );
    }

    @Test
    void 사용자를_생성한다() throws Exception {
        // given
        given(adminPageService.createUser(사용자_생성_요청)).willReturn(사용자_아아디);

        // when & then
        mockMvc.perform(post("/admin/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(사용자_생성_요청))
        ).andExpect(status().isNoContent());
    }

    @Test
    void 친구_신청한다() throws Exception {
        // given
        given(friendService.request(친구_요청_하는_사용자_아이디, 친구_요청_받는_사용자_아이디)).willReturn(친구_생성_아이디);

        // when & then
        mockMvc.perform(post("/admin/friend")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(친구_요청))
        ).andExpect(status().isNoContent());
    }

    @ParameterizedTest
    @MethodSource
    void 친구_상태를_수정한다(final UpdateFriendRequest 친구_상태_수정_요청) throws Exception {
        // given
        given(friendRepository.findByRequestUserIdAndRequestedUserId(친구_요청_하는_사용자_아이디, 친구_요청_받는_사용자_아이디))
                .willReturn(친구_아이디);

        // when & then
        mockMvc.perform(patch("/admin/friend")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(친구_상태_수정_요청))
        ).andExpect(status().isNoContent());
    }

    public static Stream<UpdateFriendRequest> 친구_상태를_수정한다() {
        return Stream.of(친구_상태_친구로_수정_요청, 친구_상태_요청_취소로_수정_요청, 친구_상태_거절로_수정_요청);
    }

    @Test
    void 친구_상태_수정시_존재하지_않는_친구_사이라면_404_예외를_발생시킨다() throws Exception {
        // given
        given(friendRepository.findByRequestUserIdAndRequestedUserId(친구_요청_하는_사용자_아이디, 친구_요청_받는_사용자_아이디))
                .willReturn(null);

        // when & then
        mockMvc.perform(patch("/admin/friend")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(친구_상태_친구로_수정_요청))
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.message").exists()
        );
    }

    @Test
    void 골을_생성한다() throws Exception {
        // given
        given(goalService.createGoal(골_생성_요청_dto)).willReturn(골_아이디);

        // when & then
        mockMvc.perform(post("/admin/goal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(골_생성_요청))
        ).andExpect(status().isNoContent());
    }
}
