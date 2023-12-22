package com.backend.blooming.goal.presentation.dto.response;

import com.backend.blooming.goal.application.dto.GoalDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

public record GoalResponse(
        Long id,
        String name,
        String memo,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        String startDate,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        String endDate,
        int days,
        Long managerId,
        List<Long> teamUserIds
) {

    public static GoalResponse from(final GoalDto goalDto) {
        return new GoalResponse(
                goalDto.id(),
                goalDto.name(),
                goalDto.memo(),
                goalDto.startDate().toString(),
                goalDto.endDate().toString(),
                goalDto.days(),
                goalDto.managerId(),
                goalDto.teamUserIds()
        );
    }
}
