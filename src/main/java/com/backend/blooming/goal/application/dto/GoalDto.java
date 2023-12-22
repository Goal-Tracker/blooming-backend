package com.backend.blooming.goal.application.dto;

import com.backend.blooming.goal.domain.Goal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record GoalDto(
        Long id,
        String name,
        String memo,
        LocalDate startDate,
        LocalDate endDate,
        int days,
        Long managerId,
        List<Long> teamUserIds
) {

    public static GoalDto from(final Goal goal) {
        final List<Long> teamUserIds = goal.getTeams().stream().map(team -> team.getUser().getId()).toList();

        return new GoalDto(
                goal.getId(),
                goal.getName(),
                goal.getMemo(),
                goal.getStartDate(),
                goal.getEndDate(),
                goal.getDays(),
                goal.getManagerId(),
                teamUserIds
        );
    }
}
