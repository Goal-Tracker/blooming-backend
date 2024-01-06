package com.backend.blooming.goal.application.dto;

import com.backend.blooming.goal.presentation.dto.request.CreateGoalRequest;

import java.time.LocalDate;
import java.util.List;

public record CreateGoalDto(
        String name,
        String memo,
        LocalDate startDate,
        LocalDate endDate,
        Long managerId,
        List<Long> teamUserIds
) {

    public static CreateGoalDto from(final CreateGoalRequest request, Long managerId) {
        return new CreateGoalDto(
                request.name(),
                request.memo(),
                request.startDate(),
                request.endDate(),
                managerId,
                request.teamUserIds()
        );
    }
}
