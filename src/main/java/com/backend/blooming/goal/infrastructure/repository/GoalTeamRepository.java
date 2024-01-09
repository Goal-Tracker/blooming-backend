package com.backend.blooming.goal.infrastructure.repository;

import com.backend.blooming.goal.domain.GoalTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GoalTeamRepository extends JpaRepository<GoalTeam, Long> {

    @Query("""
            SELECT gt
            FROM GoalTeam gt
            JOIN FETCH gt.goal
            JOIN FETCH gt.user
            WHERE gt.user.id = :userId AND gt.deleted = FALSE
            """)
    List<GoalTeam> findAllByUserId(Long userId);
}
