package com.backend.blooming.stamp.infrastructure.repository;

import com.backend.blooming.stamp.domain.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StampRepository extends JpaRepository<Stamp, Long> {

    @Query("""
                SELECT EXISTS(
                    SELECT 1
                    FROM Stamp s
                    WHERE (s.user.id = :userId AND s.day = :day) AND s.deleted = FALSE
                ) as exist
            """)
    boolean existsByUserIdAndDayAndDeletedIsFalse(final Long userId, final int day);
}
