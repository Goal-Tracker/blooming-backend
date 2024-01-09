package com.backend.blooming.goal.application.dto;

import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.dto.GoalTeamWithUserNameDto;

import java.time.LocalDate;
import java.util.List;

public record ReadAllGoalDto(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        long days,
        long inProgressDays,
        List<GoalTeamWithUserNameDto> goalTeamsWithUserName
) {

    public static ReadAllGoalDto from(final Goal goal, final List<GoalTeamWithUserNameDto> goalTeamsWithUserName) {
        return new ReadAllGoalDto(
                goal.getId(),
                goal.getName(),
                goal.getGoalTerm().getStartDate(),
                goal.getGoalTerm().getEndDate(),
                goal.getGoalTerm().getDays(),
                goal.getGoalTerm().getInProgressDays(),
                goalTeamsWithUserName
        );
    }
}
