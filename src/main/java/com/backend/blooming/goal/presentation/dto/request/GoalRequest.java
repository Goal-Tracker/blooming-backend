package com.backend.blooming.goal.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public record GoalRequest(

        @NotEmpty(message = "제목을 입력해주세요.")
        String name,
        String memo,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate,
        List<Long> teamUserIds
) {
}
