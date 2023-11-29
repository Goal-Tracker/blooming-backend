package com.backend.blooming.goal.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record GoalRequest(

        @JsonProperty("goalId")
        Long goalId,

        @JsonProperty("goalName")
        @NotEmpty(message = "제목을 입력해주세요.")
        String goalName,

        @JsonProperty("goalMemo")
        String goalMemo,

        @JsonProperty("goalStartDay")
        @NotEmpty(message = "시작 날짜를 선택해주세요.")
        String goalStartDay,

        @JsonProperty("goalEndDay")
        @NotEmpty(message = "끝나는 날짜를 선택해주세요.")
        String goalEndDay,

        @JsonProperty("goalDays")
        @NotEmpty(message = "골 날짜 수를 선택해주세요.")
        int goalDays,

        @JsonProperty("goalTeamUserIds")
        List<Long> goalTeamUserIds
) {
}
