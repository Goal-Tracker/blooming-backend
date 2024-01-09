package com.backend.blooming.goal.application.dto;

import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.dto.GoalTeamWithUserNameDto;

import java.time.LocalDate;
import java.util.List;

public record ReadGoalDetailDto(
        Long id,
        String name,
        String memo,
        LocalDate startDate,
        LocalDate endDate,
        long days,
        long inProgressDays,
        Long managerId,
        List<GoalTeamWithUserNameDto> goalTeamsWithUserName
) {

    public static ReadGoalDetailDto from(final Goal goal, final List<GoalTeamWithUserNameDto> goalTeamsWithUserNames) {
        return new ReadGoalDetailDto(
                goal.getId(),
                goal.getName(),
                goal.getMemo(),
                goal.getGoalTerm().getStartDate(),
                goal.getGoalTerm().getEndDate(),
                goal.getGoalTerm().getDays(),
                goal.getGoalTerm().getInProgressDays(),
                goal.getManagerId(),
                goalTeamsWithUserNames
        );
    }
}
