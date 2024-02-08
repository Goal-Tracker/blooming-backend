package com.backend.blooming.goal.infrastructure.repository;

import com.backend.blooming.goal.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    Optional<Goal> findByIdAndDeletedIsFalse(final Long goalId);

    @Query("""
            SELECT g
            FROM Goal g
            JOIN FETCH g.teams.goalTeams gt
            JOIN FETCH gt.user gtu
            WHERE (gtu.id = :userId AND g.deleted = FALSE)
            AND (g.goalTerm.endDate >= :now)
            ORDER BY g.goalTerm.startDate DESC
            """)
    List<Goal> findAllByUserIdAndInProgress(final Long userId, final LocalDate now);

    @Query("""
            SELECT g
            FROM Goal g
            JOIN FETCH g.teams.goalTeams gt
            JOIN FETCH gt.user gtu
            WHERE (gtu.id = :userId AND g.deleted = FALSE)
            AND (g.goalTerm.endDate < :now)
            ORDER BY g.goalTerm.startDate DESC
            """)
    List<Goal> findAllByUserIdAndFinished(final Long userId, final LocalDate now);
}
