package com.backend.blooming.goal.presentation.dto.response;

import com.backend.blooming.goal.application.dto.GoalDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

public record GoalResponse(
        Long goalId,
        String goalName,
        String goalMemo,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        String goalStartDay,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        String goalEndDay,
        int goalDays,

        @JsonProperty("goalTeamUserIds")
        List<Long> goalTeamUserIds
) {

    public static GoalResponse from(final GoalDto goalDto) {
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
