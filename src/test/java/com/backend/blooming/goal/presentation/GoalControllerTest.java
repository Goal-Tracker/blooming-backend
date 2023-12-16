package com.backend.blooming.goal.presentation;

import com.backend.blooming.goal.application.GoalService;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.goal.infrastructure.repository.GoalTeamRepository;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GoalController.class)
@AutoConfigureRestDocs
@MockBean({GoalRepository.class, GoalTeamRepository.class, UserRepository.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class GoalControllerTest extends GoalControllerTestFixture {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    GoalService goalService;

    @Test
    public void 골_생성을_요청하면_새로운_골을_생성한다() throws Exception {
        // given
        given(goalService.createGoal(유효한_골_생성_dto)).willReturn(유효한_골_dto);

        // when & then
        mockMvc.perform(post("/goals/add")
                .header("X-API-VERSION", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(요청한_골_dto))
        ).andExpectAll(
                redirectedUrl("/goals/" + 응답한_골_dto.goalId()),
                status().isCreated()
        ).andDo(document(
                requestHeaders(headerWithName("X-API-VERSION").description("요청 버전")).toString(),
                requestFields(
                        fieldWithPath("goalName").type(JsonFieldType.STRING).description("골 제목"),
                        fieldWithPath("goalMemo").type(JsonFieldType.STRING).description("골 메모"),
                        fieldWithPath("goalStartDay").type(JsonFieldType.STRING).description("골 시작날짜"),
                        fieldWithPath("goalEndDay").type(JsonFieldType.STRING).description("골 종료날짜"),
                        fieldWithPath("goalDays").type(JsonFieldType.NUMBER).description("골 날짜 수"),
                        fieldWithPath("goalTeamUserIds").type(JsonFieldType.ARRAY).description("골 팀 사용자 아이디")
                )
        ));
    }
}
