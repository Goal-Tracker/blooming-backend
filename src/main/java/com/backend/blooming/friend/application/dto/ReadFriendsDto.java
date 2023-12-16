package com.backend.blooming.friend.application.dto;

import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.user.domain.User;

import java.util.List;

public record ReadFriendsDto(List<FriendDto> friends) {

    public static ReadFriendsDto of(final List<Friend> requestUsers, final User user) {
        final List<FriendDto> friendDtos = requestUsers.stream()
                                                       .map(friend -> FriendDto.of(friend, user))
                                                       .toList();

        return new ReadFriendsDto(friendDtos);
    }

    public record FriendDto(Long id, UserDto friend, boolean isFriends) {

        public static FriendDto of(final Friend friend, final User user) {
            return new FriendDto(friend.getId(), UserDto.from(friend.getOther(user)), friend.isFriends());
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
