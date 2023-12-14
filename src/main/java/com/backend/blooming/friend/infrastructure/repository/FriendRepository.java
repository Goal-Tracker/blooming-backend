package com.backend.blooming.friend.infrastructure.repository;

import com.backend.blooming.friend.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("""
                SELECT EXISTS(
                    SELECT 1
                    FROM Friend f
                    WHERE (f.requestUser.id = :requestUserId AND f.requestedUser.id = :requestedUserId)
                    OR (f.requestUser.id = :requestedUserId AND f.requestedUser.id = :requestUserId)
                ) as exist
            """)
    boolean existsByRequestFriend(final Long requestUserId, final Long requestedUserId);

    @Query("""
        SELECT f
        FROM Friend f
        JOIN FETCH f.requestedUser
        WHERE f.requestUser.id = :userId
    """)
    List<Friend> findAllByRequestUserId(final Long userId);
}
