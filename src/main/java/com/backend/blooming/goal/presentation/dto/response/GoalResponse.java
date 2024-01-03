package com.backend.blooming.goal.presentation.dto.response;

import com.backend.blooming.goal.application.dto.GoalDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record GoalResponse(

        Long id,
        String name,
        String memo,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate endDate,
        long days,
        Long managerId,
        List<Long> teamUserIds
) {

    public static GoalResponse from(final GoalDto goalDto) {
        return new GoalResponse(
                goalDto.id(),
                goalDto.name(),
                goalDto.memo(),
                goalDto.startDate(),
                goalDto.endDate(),
                goalDto.days(),
                goalDto.managerId(),
                goalDto.teamUserIds()
        );
    }
}
