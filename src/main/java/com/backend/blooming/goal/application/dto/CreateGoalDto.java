package com.backend.blooming.goal.application.dto;

import com.backend.blooming.goal.presentation.dto.request.GoalRequest;

import java.util.List;

public record CreateGoalDto(
        Long goalId,
        String goalName,
        String goalMemo,
        String goalStartDay,
        String goalEndDay,
        int goalDays,
        List<Long> goalTeamUserIds
) {

    public static CreateGoalDto from(final GoalRequest request) {
        return new CreateGoalDto(
                request.goalId(),
                request.goalName(),
                request.goalMemo(),
                request.goalStartDay(),
                request.goalEndDay(),
                request.goalDays(),
                request.goalTeamUserIds()
        );
    }
}
