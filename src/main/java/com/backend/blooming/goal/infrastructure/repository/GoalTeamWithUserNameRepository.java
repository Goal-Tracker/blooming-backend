package com.backend.blooming.goal.infrastructure.repository;

import com.backend.blooming.goal.infrastructure.repository.dto.GoalTeamWithUserNameDto;
import com.backend.blooming.goal.infrastructure.repository.dto.GoalTeamWithUserQueryProjectionDto;

import java.util.List;

public interface GoalTeamWithUserNameRepository {

    List<GoalTeamWithUserNameDto> findAllByGoalIdAndDeletedIsFalse(Long goalId);
}
