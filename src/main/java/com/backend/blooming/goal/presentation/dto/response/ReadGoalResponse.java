package com.backend.blooming.goal.presentation.dto.response;

import com.backend.blooming.goal.application.dto.ReadGoalDetailDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record ReadGoalResponse(

        Long id,
        String name,
        String memo,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate endDate,
        long days,
        Long managerId,
        List<GoalTeamResponse> teams
) {

    public static ReadGoalResponse from(final ReadGoalDetailDto readGoalDetailDto) {

        final List<GoalTeamResponse> goalTeamWithUserInfoResponses = readGoalDetailDto.teams()
                                                                                      .stream()
                                                                                      .map(GoalTeamResponse::from)
                                                                                      .toList();

        return new ReadGoalResponse(
                readGoalDetailDto.id(),
                readGoalDetailDto.name(),
                readGoalDetailDto.memo(),
                readGoalDetailDto.startDate(),
                readGoalDetailDto.endDate(),
                readGoalDetailDto.days(),
                readGoalDetailDto.managerId(),
                goalTeamWithUserInfoResponses
        );
    }

    public record GoalTeamResponse(
            Long id,
            String name,
            String colorCode,
            String statusMessage
    ) {

        public static GoalTeamResponse from(final ReadGoalDetailDto.GoalTeamDto goalTeamDto) {
            return new GoalTeamResponse(
                    goalTeamDto.id(),
                    goalTeamDto.name(),
                    goalTeamDto.color().getCode(),
                    goalTeamDto.statusMessage()
            );
        }
    }
}
