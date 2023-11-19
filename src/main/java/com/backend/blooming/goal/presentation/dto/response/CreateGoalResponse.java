package com.backend.blooming.goal.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateGoalResponse(
        @JsonProperty("goalId")
        String goalId,

        @JsonProperty("goalName")
        String goalName,

        @JsonProperty("goalMemo")
        String goalMemo,

        @JsonProperty("goalStartDay")
        String goalStartDay,

        @JsonProperty("goalEndDay")
        String goalEndDay,

        @JsonProperty("goalDays")
        int goalDays,

        @JsonProperty("goalTeamUserNames")
        List<String> goalTeamUserIds
) {
}
