package com.backend.blooming.goal.application.dto;

import com.backend.blooming.goal.domain.Goal;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record GoalDto(
        Long id,
        String name,
        String memo,
        LocalDate startDate,
        LocalDate endDate,
        long days,
        long inProgressDays,
        Long managerId,
        List<Long> teamUserIds
) {

    public static GoalDto from(final Goal goal) {
        final List<Long> teamUserIds = goal.getTeams()
                                           .stream()
                                           .map(team -> team.getUser().getId())
                                           .collect(Collectors.toList());

        return new GoalDto(
                goal.getId(),
                goal.getName(),
                goal.getMemo(),
                goal.getGoalTerm().getStartDate(),
                goal.getGoalTerm().getEndDate(),
                goal.getGoalTerm().getDays(),
                goal.getGoalTerm().getInProgressDays(),
                goal.getManagerId(),
                teamUserIds
        );
    }
}
