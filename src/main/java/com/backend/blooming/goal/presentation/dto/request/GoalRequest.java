package com.backend.blooming.goal.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record GoalRequest(

        @NotEmpty(message = "제목을 입력해주세요.")
        String goalName,
        String goalMemo,

        @NotEmpty(message = "시작 날짜를 선택해주세요.")
        String goalStartDay,

        @NotEmpty(message = "끝나는 날짜를 선택해주세요.")
        String goalEndDay,

        @NotEmpty(message = "골 날짜수를 입력해주세요.")
        int goalDays,

        @NotEmpty
        Long goalManagerId,
        List<Long> goalTeamUserIds
) {
}
