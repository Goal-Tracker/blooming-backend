package com.backend.blooming.user.infrastructure.repository.dto;

import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.user.domain.User;

public record UserWithFriendsStatusDto(User user, FriendsStatus friendsStatus) {

    public static UserWithFriendsStatusDto of(final User currentUser, final User user, final Friend friend) {
        return new UserWithFriendsStatusDto(user, processFriendsStatus(currentUser, user, friend));
    }

    private static FriendsStatus processFriendsStatus(final User currentUser, final User user, final Friend friend) {
        if (currentUser.equals(user)) {
            return FriendsStatus.SELF;
        }
        if (friend == null) {
            return FriendsStatus.NONE;
        }
        if (friend.isFriends()) {
            return FriendsStatus.FRIENDS;
        }
        if (friend.isRequestedUser(currentUser)) {
            return FriendsStatus.REQUESTED;
        }

        return FriendsStatus.REQUEST;
    }
}
