package com.backend.blooming.goal.presentation.dto.response;

import com.backend.blooming.goal.application.dto.ReadAllGoalDto;

import java.time.LocalDate;
import java.util.List;

public record ReadAllGoalResponse(List<GoalInfoResponse> goals) {

    public static ReadAllGoalResponse from(final ReadAllGoalDto readAllGoalDto) {
        final List<GoalInfoResponse> goalInfoResponses = readAllGoalDto.goalInfoDtos()
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
            List<GoalTeamWithUserInfoResponse> goalTeamWithUserInfos) {

        public static GoalInfoResponse from(final ReadAllGoalDto.GoalInfoDto goalInfoDto) {
            final List<GoalTeamWithUserInfoResponse> goalTeamWithUserInfoResponses = goalInfoDto.goalTeamWithUserInfoDtos()
                                                                                                .stream()
                                                                                                .map(GoalTeamWithUserInfoResponse::from)
                                                                                                .toList();

            return new GoalInfoResponse(
                    goalInfoDto.id(),
                    goalInfoDto.name(),
                    goalInfoDto.startDate(),
                    goalInfoDto.endDate(),
                    goalInfoDto.days(),
                    goalTeamWithUserInfoResponses);
        }
    }

    public record GoalTeamWithUserInfoResponse(
            Long id,
            String name,
            String colorCode) {

        public static GoalTeamWithUserInfoResponse from(final ReadAllGoalDto.GoalInfoDto.GoalTeamWithUserInfoDto goalTeamWithUserInfoDto) {
            return new GoalTeamWithUserInfoResponse(
                    goalTeamWithUserInfoDto.id(),
                    goalTeamWithUserInfoDto.name(),
                    goalTeamWithUserInfoDto.color().getCode());
        }
    }
}
