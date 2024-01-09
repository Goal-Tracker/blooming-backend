package com.backend.blooming.goal.presentation.dto.response;

import com.backend.blooming.goal.application.dto.ReadAllGoalDto;

import java.util.List;

public record ReadAllGoalResponse(
        List<ReadAllGoalDto> readAllGoals
) {
}
