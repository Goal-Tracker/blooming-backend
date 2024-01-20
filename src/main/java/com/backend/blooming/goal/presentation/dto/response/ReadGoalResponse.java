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
        List<GoalTeamWithUserInfoResponse> goalTeamWithUserInfo
) {

    public static ReadGoalResponse from(final ReadGoalDetailDto readGoalDetailDto) {

        final List<GoalTeamWithUserInfoResponse> goalTeamWithUserInfoResponses = readGoalDetailDto.GoalTeamWithUserInfo()
                                                                                                  .stream()
                                                                                                  .map(GoalTeamWithUserInfoResponse::from)
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

    public record GoalTeamWithUserInfoResponse(Long id, String name,
                                               String colorCode,
                                               String statusMessage) {

        public static GoalTeamWithUserInfoResponse from(final ReadGoalDetailDto.GoalTeamWithUserInfoDto goalTeamWithUserInfoDto) {

            return new GoalTeamWithUserInfoResponse(
                    goalTeamWithUserInfoDto.id(),
                    goalTeamWithUserInfoDto.name(),
                    goalTeamWithUserInfoDto.color().getCode(),
                    goalTeamWithUserInfoDto.statusMessage());
        }
    }
}
