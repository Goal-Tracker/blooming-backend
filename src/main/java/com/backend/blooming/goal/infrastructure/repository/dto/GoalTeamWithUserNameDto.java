package com.backend.blooming.goal.infrastructure.repository.dto;

import com.backend.blooming.themecolor.domain.ThemeColor;
import com.querydsl.core.annotations.QueryProjection;

public record GoalTeamWithUserNameDto(
        Long userId,
        String userName,
        ThemeColor userColor
) {

    @QueryProjection
    public GoalTeamWithUserNameDto {
    }
}
