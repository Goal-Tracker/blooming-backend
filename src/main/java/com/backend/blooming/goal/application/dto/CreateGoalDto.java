package com.backend.blooming.goal.application.dto;

import com.backend.blooming.goal.presentation.dto.request.GoalRequest;

import java.util.List;

public record CreateGoalDto(
        String name,
        String memo,
        String startDate,
        String endDate,
        int days,
        Long managerId,
        List<Long> teamUserIds
) {

    public static CreateGoalDto from(final GoalRequest request, Long managerId) {
        return new CreateGoalDto(
                request.name(),
                request.memo(),
                request.startDate(),
                request.endDate(),
                request.days(),
                managerId,
                request.teamUserIds()
        );
    }
}
