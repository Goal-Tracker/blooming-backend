package com.backend.blooming.goal.infrastructure.repository;

import com.backend.blooming.goal.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Long> {
}
