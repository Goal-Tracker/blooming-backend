package com.backend.blooming.goal.presentation;

import com.backend.blooming.goal.application.GoalService;
import com.backend.blooming.goal.presentation.dto.request.CreateGoalRequest;
import com.backend.blooming.goal.presentation.dto.response.CreateGoalResponse;
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
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(GoalController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GoalService goalService;

    @Test
    public void 골_생성_컨트롤러() throws Exception {
        // given
        List<String> goalTeamUserIds = new ArrayList<>(List.of("1","2","3","4"));

        CreateGoalRequest request = new CreateGoalRequest(
                "골 제목",
                "골 메모",
                "2023-11-17",
                "2023-12-31",
                45,
                goalTeamUserIds
        );

        CreateGoalResponse response = new CreateGoalResponse(
                "1",
                "골 제목",
                "골 메모",
                "2023-11-17",
                "2023-12-31",
                45,
                goalTeamUserIds);

        given(goalService.createGoalResponse(goalService.createGoalDto(request)))
                .willReturn(response);

        // when
        ResultActions result = this.mockMvc.perform(post("/goals/add")
                .content(objectMapper.writeValueAsString(response))
                .header("X-API-VERSION","1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(redirectedUrl("/goals/1"))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("goals-add",
                        getDocumentRequest(),
                        getDocumentResponse(),
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