package com.backend.blooming.stamp.infrastructure.repository;

import com.backend.blooming.stamp.domain.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StampRepository extends JpaRepository<Stamp, Long> {

    @Query("""
                SELECT EXISTS(
                    SELECT 1
                    FROM Stamp s
                    WHERE (s.user.id = :userId AND s.day.day = :day) AND s.deleted = FALSE
                ) as exist
            """)
    boolean existsByUserIdAndDayAndDeletedIsFalse(final Long userId, final int day);

    @Query("""
            SELECT s
            FROM Stamp s
            JOIN FETCH s.user
            JOIN FETCH s.goal sg
            WHERE (sg.id = :goalId) AND s.deleted = FALSE
            """)
    List<Stamp> findAllByGoalIdAndDeletedIsFalse(final Long goalId);

    @Query("""
            SELECT s
            FROM Stamp s
            JOIN FETCH s.user
            WHERE s.day.day = :day AND s.deleted = FALSE
            """)
    List<Stamp> findAllByDayAndDeletedIsFalse(final int day);
}
