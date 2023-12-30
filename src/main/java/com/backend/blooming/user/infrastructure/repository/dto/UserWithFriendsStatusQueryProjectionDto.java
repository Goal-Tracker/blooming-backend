package com.backend.blooming.user.infrastructure.repository.dto;

import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.user.domain.User;
import com.querydsl.core.annotations.QueryProjection;

public record UserWithFriendsStatusQueryProjectionDto(User user, Friend friend) {

    @QueryProjection
    public UserWithFriendsStatusQueryProjectionDto {
    }
}
