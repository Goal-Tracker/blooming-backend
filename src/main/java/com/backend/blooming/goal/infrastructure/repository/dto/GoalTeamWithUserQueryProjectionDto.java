package com.backend.blooming.goal.infrastructure.repository.dto;

import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.user.domain.User;
import com.querydsl.core.annotations.QueryProjection;

public record GoalTeamWithUserQueryProjectionDto(
        GoalTeam goalTeam,
        Goal goal,
        User user
) {

    @QueryProjection
    public GoalTeamWithUserQueryProjectionDto {
    }
}
