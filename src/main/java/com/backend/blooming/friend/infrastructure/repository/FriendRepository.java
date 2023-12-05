package com.backend.blooming.friend.infrastructure.repository;

import com.backend.blooming.friend.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    boolean existsByRequestUserIdAndRequestedUserId(final Long requestUserId, final Long requestedUserId);
}
