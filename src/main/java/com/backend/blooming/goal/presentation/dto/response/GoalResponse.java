package com.backend.blooming.goal.presentation.dto.response;

import com.backend.blooming.goal.application.dto.GoalDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GoalResponse(
        @JsonProperty("goalId")
        Long goalId,

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
        List<Long> goalTeamUserIds
) {
        public static GoalResponse from(final GoalDto goalDto){
                return new GoalResponse(
                        goalDto.goalId(),
                        goalDto.goalName(),
                        goalDto.goalMemo(),
                        goalDto.goalStartDay().toString(),
                        goalDto.goalEndDay().toString(),
                        goalDto.goalDays(),
                        goalDto.goalTeamUserIds()
                );
        }
}
