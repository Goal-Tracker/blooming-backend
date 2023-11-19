package com.backend.blooming.goal.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateGoalRequest(
        @NotEmpty(message = "제목을 입력해주세요.")
        String goalName,
        String goalMemo,
        @NotEmpty(message = "시작 날짜를 선택해주세요.")
        String goalStartDay,
        @NotEmpty(message = "끝나는 날짜를 선택해주세요.")
        String goalEndDay,
        @NotEmpty(message = "골 날짜 수를 선택해주세요.")
        int goalDays,
        List<String> goalTeamUserIds
) {
}
