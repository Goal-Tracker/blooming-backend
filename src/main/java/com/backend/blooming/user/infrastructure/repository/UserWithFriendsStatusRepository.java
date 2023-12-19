package com.backend.blooming.user.infrastructure.repository;

import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.dto.UserWithFriendsStatusDto;

import java.util.List;

public interface UserWithFriendsStatusRepository {

    List<UserWithFriendsStatusDto> findAllByNameContainsAndDeletedIsFalse(final User user, final String keyword);
}
