package com.backend.blooming.goal.application.dto;

import com.backend.blooming.goal.presentation.dto.request.UpdateGoalRequest;

import java.time.LocalDate;
import java.util.List;

public record UpdateGoalDto(
        String name,
        String memo,
        LocalDate endDate,
        List<Long> teamUserIds
) {

    public static UpdateGoalDto from(final UpdateGoalRequest request) {
        return new UpdateGoalDto(
                request.name(),
                request.memo(),
                request.endDate(),
                request.teamUserIds()
        );
    }
}
