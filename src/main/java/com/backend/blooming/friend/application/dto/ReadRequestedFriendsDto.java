package com.backend.blooming.friend.application.dto;

import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.user.domain.User;

import java.util.List;

public record ReadRequestedFriendsDto(List<FriendDto> friends) {

    public static ReadRequestedFriendsDto from(final List<Friend> requestedUsers) {
        final List<FriendDto> friendDtos = requestedUsers.stream()
                                                       .map(FriendDto::from)
                                                       .toList();

        return new ReadRequestedFriendsDto(friendDtos);
    }

    public record FriendDto(Long id, UserDto friend, boolean isFriends) {

        public static FriendDto from(final Friend friend) {
            return new FriendDto(friend.getId(), UserDto.from(friend.getRequestedUser()), friend.isFriends());
        }

        public record UserDto(
                Long id,
                String oAuthId,
                String oAuthType,
                String email,
                String name,
                String color,
                String statusMessage
        ) {

            public static UserDto from(final User requestedUser) {
                return new UserDto(
                        requestedUser.getId(),
                        requestedUser.getOAuthId(),
                        requestedUser.getOAuthType().name(),
                        requestedUser.getEmail(),
                        requestedUser.getName(),
                        requestedUser.getColorCode(),
                        requestedUser.getStatusMessage()
                );
            }
        }
    }
}
