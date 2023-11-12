package com.backend.blooming.goal.application.dto;

import java.util.List;

public record CreateGoalDto(
        String goalName,
        String goalMemo,
        String goalStartDay,
        String goalEndDay,
        int goalDays,
        List<Long> goalTeamUserIds
) {
}
