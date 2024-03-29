package com.backend.blooming.goal.presentation.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public record UpdateGoalRequest(
        
        String name,
        String memo,
        
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @FutureOrPresent(message = "종료날짜는 현재보다 과거일 수 없습니다.")
        LocalDate endDate,
        List<Long> teamUserIds
) {
}
