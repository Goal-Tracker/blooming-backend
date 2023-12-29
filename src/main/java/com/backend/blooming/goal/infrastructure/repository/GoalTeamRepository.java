package com.backend.blooming.goal.infrastructure.repository;

import com.backend.blooming.goal.domain.GoalTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalTeamRepository extends JpaRepository<GoalTeam, Long> {
}
