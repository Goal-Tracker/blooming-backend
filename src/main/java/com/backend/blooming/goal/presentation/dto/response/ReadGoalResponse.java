package com.backend.blooming.goal.presentation.dto.response;

import com.backend.blooming.goal.application.dto.GoalDto;
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
        List<Long> teamUserIds
) {

    public static ReadGoalResponse from(final GoalDto goalDto) {
        return new ReadGoalResponse(
                goalDto.id(),
                goalDto.name(),
                goalDto.memo(),
                goalDto.startDate(),
                goalDto.endDate(),
                goalDto.days(),
                goalDto.inProgressDays(),
                goalDto.managerId(),
                goalDto.teamUserIds()
        );
    }
}
