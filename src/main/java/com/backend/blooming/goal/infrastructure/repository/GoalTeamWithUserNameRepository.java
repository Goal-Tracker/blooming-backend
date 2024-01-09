package com.backend.blooming.goal.infrastructure.repository;

import com.backend.blooming.goal.infrastructure.repository.dto.GoalTeamWithUserNameDto;

import java.util.List;

public interface GoalTeamWithUserNameRepository {

    List<GoalTeamWithUserNameDto> findAllByGoalIdAndDeletedIsFalse(Long goalId);
}
