package com.backend.blooming.goal.presentation.dto.response;

import com.backend.blooming.goal.application.dto.ReadAllGoalDto;

import java.time.LocalDate;
import java.util.List;

public record ReadAllGoalResponse(List<GoalInfoResponse> goals) {

    public static ReadAllGoalResponse from(final ReadAllGoalDto readAllGoalDto) {
        final List<GoalInfoResponse> goalInfoResponses = readAllGoalDto.goalInfos()
                                                                       .stream()
                                                                       .map(GoalInfoResponse::from)
                                                                       .toList();

        return new ReadAllGoalResponse(goalInfoResponses);
    }

    public record GoalInfoResponse(
            Long id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            long days,
            List<GoalTeamResponse> teams
    ) {

        public static GoalInfoResponse from(final ReadAllGoalDto.GoalInfoDto goalInfoDto) {
            final List<GoalTeamResponse> teams = goalInfoDto.teams()
                                                            .stream()
                                                            .map(GoalTeamResponse::from)
                                                            .toList();

            return new GoalInfoResponse(
                    goalInfoDto.id(),
                    goalInfoDto.name(),
                    goalInfoDto.startDate(),
                    goalInfoDto.endDate(),
                    goalInfoDto.days(),
                    teams);
        }
    }

    public record GoalTeamResponse(
            Long id,
            String name,
            String colorCode
    ) {

        public static GoalTeamResponse from(final ReadAllGoalDto.GoalInfoDto.GoalTeamDto teams) {
            return new GoalTeamResponse(
                    teams.id(),
                    teams.name(),
                    teams.color().getCode());
        }
    }
}
