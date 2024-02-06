package com.backend.blooming.admin.controller;

import com.backend.blooming.admin.application.AdminPageService;
import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedThreadLocal;
import com.backend.blooming.friend.application.FriendService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AdminPageController.class)
@Import(AuthenticatedThreadLocal.class)
@MockBean({TokenProvider.class, UserRepository.class})
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminPageControllerTest extends AdminPageControllerTestFixture {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminPageService adminPageService;

    @MockBean
    private FriendService friendService;

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
        mockMvc.perform(post("/admin/friend/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(친구_요청))
        ).andExpect(status().isNoContent());
    }
}
