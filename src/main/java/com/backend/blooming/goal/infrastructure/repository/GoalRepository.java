package com.backend.blooming.goal.infrastructure.repository;

import com.backend.blooming.goal.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    Optional<Goal> findByIdAndDeletedIsFalse(final Long goalId);
}
