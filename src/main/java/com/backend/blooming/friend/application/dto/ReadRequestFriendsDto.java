package com.backend.blooming.friend.application.dto;

import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.user.domain.User;

import java.util.List;

public record ReadRequestFriendsDto(List<FriendDto> friends) {

    public static ReadRequestFriendsDto from(final List<Friend> requestUsers) {
        final List<FriendDto> friendDtos = requestUsers.stream()
                                                       .map(FriendDto::from)
                                                       .toList();

        return new ReadRequestFriendsDto(friendDtos);
    }

    public record FriendDto(Long id, UserDto friend, boolean isFriends) {

        public static FriendDto from(final Friend friend) {
            return new FriendDto(friend.getId(), UserDto.from(friend.getRequestUser()), friend.isFriends());
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

            public static UserDto from(final User requestUser) {
                return new UserDto(
                        requestUser.getId(),
                        requestUser.getOAuthId(),
                        requestUser.getOAuthType().name(),
                        requestUser.getEmail(),
                        requestUser.getName(),
                        requestUser.getColorCode(),
                        requestUser.getStatusMessage()
                );
            }
        }
    }
}
