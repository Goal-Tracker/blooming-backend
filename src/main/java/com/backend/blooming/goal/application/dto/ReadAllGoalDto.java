package com.backend.blooming.goal.application.dto;

import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.themecolor.domain.ThemeColor;

import java.time.LocalDate;
import java.util.List;

public record ReadAllGoalDto(List<GoalInfoDto> goalInfos) {

    public static ReadAllGoalDto from(final List<Goal> goals) {
        final List<GoalInfoDto> goalInfos = goals.stream()
                                                 .map(GoalInfoDto::from)
                                                 .toList();

        return new ReadAllGoalDto(goalInfos);
    }

    public record GoalInfoDto(
            Long id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            long days,
            List<GoalTeamDto> teams
    ) {

        public static GoalInfoDto from(final Goal goal) {
            final List<GoalTeamDto> teams = goal.getTeams()
                                                .stream()
                                                .map(GoalTeamDto::from)
                                                .toList();

            return new GoalInfoDto(
                    goal.getId(),
                    goal.getName(),
                    goal.getGoalTerm().getStartDate(),
                    goal.getGoalTerm().getEndDate(),
                    goal.getGoalTerm().getDays(),
                    teams
            );
        }

        public record GoalTeamDto(Long id, String name, ThemeColor color) {

            public static GoalTeamDto from(final GoalTeam goalTeam) {
                return new GoalTeamDto(
                        goalTeam.getUser().getId(),
                        goalTeam.getUser().getName(),
                        goalTeam.getUser().getColor()
                );
            }
        }
    }
}
