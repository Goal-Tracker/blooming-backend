package com.backend.blooming.goal.presentation.dto.response;

import com.backend.blooming.goal.application.dto.ReadGoalDetailDto;
import com.backend.blooming.goal.infrastructure.repository.dto.GoalTeamWithUserNameDto;
import com.backend.blooming.goal.infrastructure.repository.dto.GoalTeamWithUserQueryProjectionDto;
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
        long inProgressDays,
        Long managerId,
        List<GoalTeamWithUserNameDto> goalTeamsWithUserName
) {

    public static ReadGoalResponse from(final ReadGoalDetailDto readGoalDetailDto) {
        return new ReadGoalResponse(
                readGoalDetailDto.id(),
                readGoalDetailDto.name(),
                readGoalDetailDto.memo(),
                readGoalDetailDto.startDate(),
                readGoalDetailDto.endDate(),
                readGoalDetailDto.days(),
                readGoalDetailDto.inProgressDays(),
                readGoalDetailDto.managerId(),
                readGoalDetailDto.goalTeamsWithUserName()
        );
    }
}
