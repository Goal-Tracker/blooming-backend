package com.backend.blooming.goal.presentation.dto.response;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateGoalResponse(
        String goalId,
        String goalName,
        String goalMemo,
        String goalStartDay,
        String goalEndDay,
        int goalDays,
        List<String> goalTeamUserIds
) {
}
