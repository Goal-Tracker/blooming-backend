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
                WHERE f.requestUser.id = :userId AND f.isFriends = FALSE
            """)
    List<Friend> findAllByRequestUserId(final Long userId);

    @Query("""
                SELECT f
                FROM Friend f
                JOIN FETCH f.requestUser
                WHERE f.requestedUser.id = :userId AND f.isFriends = FALSE
            """)
    List<Friend> findAllByRequestedUserId(final Long userId);

    @Query("""
                SELECT f
                FROM Friend f
                JOIN FETCH f.requestUser
                JOIN FETCH f.requestedUser
                WHERE (f.requestUser.id = :userId OR f.requestedUser.id = :userId) AND f.isFriends = TRUE
            """)
    List<Friend> findAllByUserIdAndIsFriends(final Long userId);

    @Query("""
            SELECT COUNT (*)
                FROM Friend f
                WHERE (f.requestUser.id = :userId AND f.requestedUser.id = :friendId) IN :friendIds
                OR (f.requestUser.id = :friendId AND f.requestedUser.id = :userId) IN :friendIds
                AND f.isFriends = TRUE
            """)
    Long countByUserIdAndFriendIdsAndIsFriends(final Long userId, final List<Long> friendIds);
}
