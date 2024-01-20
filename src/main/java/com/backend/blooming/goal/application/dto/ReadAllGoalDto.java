package com.backend.blooming.goal.application.dto;

import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.themecolor.domain.ThemeColor;

import java.time.LocalDate;
import java.util.List;

public record ReadAllGoalDto(List<GoalInfoDto> goalInfoDtos) {

    public static ReadAllGoalDto from(final List<Goal> goals) {

        final List<GoalInfoDto> goalInfoDtos = goals.stream()
                                                    .map(GoalInfoDto::from)
                                                    .toList();

        return new ReadAllGoalDto(goalInfoDtos);
    }

    public record GoalInfoDto(Long id, String name, LocalDate startDate,
                              LocalDate endDate, long days,
                              List<GoalTeamWithUserInfoDto> goalTeamWithUserInfoDtos) {

        public static GoalInfoDto from(final Goal goal) {

            final List<GoalTeamWithUserInfoDto> goalTeamWithUserInfoDtos = goal.getTeams()
                                                                               .stream()
                                                                               .map(GoalTeamWithUserInfoDto::from)
                                                                               .toList();

            return new GoalInfoDto(
                    goal.getId(),
                    goal.getName(),
                    goal.getGoalTerm().getStartDate(),
                    goal.getGoalTerm().getEndDate(),
                    goal.getGoalTerm().getDays(),
                    goalTeamWithUserInfoDtos
            );
        }

        public record GoalTeamWithUserInfoDto(Long id, String name,
                                              ThemeColor color) {

            public static GoalTeamWithUserInfoDto from(final GoalTeam goalTeam) {
                return new GoalTeamWithUserInfoDto(
                        goalTeam.getId(),
                        goalTeam.getUser().getName(),
                        goalTeam.getUser().getColor()
                );
            }
        }
    }
}
