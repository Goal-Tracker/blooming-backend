package com.backend.blooming.goal.presentation.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public record UpdateGoalRequest(

        String name,
        String memo,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @FutureOrPresent
        LocalDate endDate,
        List<Long> teamUserIds
) {
}
