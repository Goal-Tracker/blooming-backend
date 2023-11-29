package com.backend.blooming.goal.presentation;

import com.backend.blooming.goal.application.GoalService;
import com.backend.blooming.goal.application.dto.GoalDto;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.goal.infrastructure.repository.GoalTeamRepository;
import com.backend.blooming.goal.presentation.dto.response.GoalResponse;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.backend.blooming.utils.ApiDocumentUtils.getDocumentRequest;
import static com.backend.blooming.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GoalController.class)
@AutoConfigureRestDocs
@MockBean({GoalRepository.class, GoalTeamRepository.class, GoalResponse.class, UserRepository.class})
@SuppressWarnings("NonAsciiCharacters")
class GoalControllerTest extends GoalControllerFixture {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    GoalService goalService;

    @Test
    public void 골_생성을_요청하면_생성된_골의_정보를_반환한다() throws Exception {
        // given
        final GoalDto goalDto = goalService.createGoal(유효한_골_생성_dto);
        System.out.println(goalDto.goalId());
        given(GoalResponse.from(goalDto)).willReturn(응답한_골_dto);

        // when & then
        mockMvc.perform(post("/goals/add")
                .content(objectMapper.writeValueAsString(요청한_골_dto))
                .header("X-API-VERSION", "1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                redirectedUrl("/goals/" + 응답한_골_dto.goalId()),
                status().isCreated()
        ).andDo(document(
                requestHeaders(headerWithName("X-API-VERSION").description("요청 버전")).toString(),
                requestFields(
                        fieldWithPath("goalId").type(JsonFieldType.STRING).description("골 아이디"),
                        fieldWithPath("goalName").type(JsonFieldType.STRING).description("골 제목"),
                        fieldWithPath("goalMemo").type(JsonFieldType.STRING).description("골 메모"),
                        fieldWithPath("goalStartDay").type(JsonFieldType.STRING).description("골 시작날짜"),
                        fieldWithPath("goalEndDay").type(JsonFieldType.STRING).description("골 종료날짜"),
                        fieldWithPath("goalDays").type(JsonFieldType.NUMBER).description("골 날짜 수"),
                        fieldWithPath("goalTeamUserIds").type(JsonFieldType.ARRAY).description("골 팀 사용자 아이디")
                ),
                responseFields(
                        fieldWithPath("goalId").type(JsonFieldType.STRING).description("골 아이디"),
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
