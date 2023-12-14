package com.backend.blooming.friend.application.dto;

import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.domain.User;

import java.util.List;

public record ReadFriendsDto(List<FriendDto> friends) {

    public static ReadFriendsDto from(final List<Friend> requestUsers) {
        final List<FriendDto> friendDtos = requestUsers.stream()
                                                       .map(FriendDto::from)
                                                       .toList();

        return new ReadFriendsDto(friendDtos);
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
                        processColorCode(requestedUser.getColor()),
                        requestedUser.getStatusMessage()
                );
            }
        }

        private static String processColorCode(final ThemeColor color) {
            if (color == null) {
                return null;
            }

            return color.getCode();
        }
    }
}
