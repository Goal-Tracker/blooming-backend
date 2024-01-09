package com.backend.blooming.goal.infrastructure.repository.dto;

import com.backend.blooming.themecolor.domain.ThemeColor;

public record GoalTeamWithUserNameDto(
        Long userId,
        String userName,
        ThemeColor userColor
) {
}
