package com.backend.blooming.goal.application.dto;

import com.backend.blooming.goal.domain.Goal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record GoalDto(
        Long goalId,
        String goalName,
        String goalMemo,
        LocalDate goalStartDay,
        LocalDate goalEndDay,
        int goalDays,
        List<Long> goalTeamUserIds
) {

    public static GoalDto from(final Goal goal) {
        final List<Long> goalTeamUserIds = new ArrayList<>();
        goal.getGoalTeams().forEach(goalTeam -> goalTeamUserIds.add(goalTeam.getUser().getId()));

        return new GoalDto(
                goal.getId(),
                goal.getGoalName(),
                goal.getGoalMemo(),
                goal.getGoalStartDay(),
                goal.getGoalEndDay(),
                goal.getGoalDays(),
                goalTeamUserIds
        );
    }
}
