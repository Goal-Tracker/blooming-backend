package com.backend.blooming.goal.application.dto;

import com.backend.blooming.goal.domain.Goal;

import java.util.Date;
import java.util.List;

public record GoalDto(
        Long goalId,
        String goalName,
        String goalMemo,
        Date goalStartDay,
        Date goalEndDay,
        int goalDays,
        List<Long> goalTeamUserIds
) {
    public static GoalDto from(final Goal goal) {
        return new GoalDto(
                goal.getId(),
                goal.getGoalName(),
                goal.getGoalMemo(),
                goal.getGoalStartDay(),
                goal.getGoalEndDay(),
                goal.getGoalDays(),
                goal.getGoalTeamIds());
    }
}
