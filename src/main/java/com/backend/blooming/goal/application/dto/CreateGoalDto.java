package com.backend.blooming.goal.application.dto;

public record CreateGoalDto(
        String goalName,
        String goalMemo,
        String goalStartDay,
        String goalEndDay,
        int goalDays
) {
}
