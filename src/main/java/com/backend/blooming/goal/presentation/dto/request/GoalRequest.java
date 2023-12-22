package com.backend.blooming.goal.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record GoalRequest(

        @NotEmpty(message = "제목을 입력해주세요.")
        String name,
        String memo,

        @NotEmpty(message = "시작 날짜를 선택해주세요.")
        String startDate,

        @NotEmpty(message = "끝나는 날짜를 선택해주세요.")
        String endDate,

        @NotEmpty(message = "골 날짜수를 입력해주세요.")
        int days,
        List<Long> teamUserIds
) {
}
