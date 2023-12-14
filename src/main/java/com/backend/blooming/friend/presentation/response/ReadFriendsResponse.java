package com.backend.blooming.friend.presentation.response;

import com.backend.blooming.friend.application.dto.ReadFriendsDto;

import java.util.List;

public record ReadFriendsResponse(List<FriendResponse> friends) {

    public static ReadFriendsResponse from(final ReadFriendsDto friendsDto) {
        final List<FriendResponse> friendResponses = friendsDto.friends()
                                                              .stream()
                                                              .map(FriendResponse::from)
                                                              .toList();

        return new ReadFriendsResponse(friendResponses);
    }

    public record FriendResponse(Long id, UserResponse friend, boolean isFriends) {

        public static FriendResponse from(final ReadFriendsDto.FriendDto friendDto) {
            return new FriendResponse(
                    friendDto.id(),
                    UserResponse.from(friendDto.friend()),
                    friendDto.isFriends()
            );
        }

        public record UserResponse(Long id, String email, String name, String color, String statusMessage) {

            public static UserResponse from(final ReadFriendsDto.FriendDto.UserDto user) {
                return new UserResponse(user.id(), user.email(), user.name(), user.color(), user.statusMessage());
            }
        }
    }
}
