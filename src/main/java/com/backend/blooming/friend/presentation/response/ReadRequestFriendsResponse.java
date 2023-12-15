package com.backend.blooming.friend.presentation.response;

import com.backend.blooming.friend.application.dto.ReadRequestFriendsDto;

import java.util.List;

public record ReadRequestFriendsResponse(List<FriendResponse> friends) {

    public static ReadRequestFriendsResponse from(final ReadRequestFriendsDto friendsDto) {
        final List<FriendResponse> friendResponses = friendsDto.friends()
                                                               .stream()
                                                               .map(FriendResponse::from)
                                                               .toList();

        return new ReadRequestFriendsResponse(friendResponses);
    }

    public record FriendResponse(Long id, UserResponse friend, boolean isFriends) {

        public static FriendResponse from(final ReadRequestFriendsDto.FriendDto friendDto) {
            return new FriendResponse(
                    friendDto.id(),
                    UserResponse.from(friendDto.friend()),
                    friendDto.isFriends()
            );
        }

        public record UserResponse(Long id, String email, String name, String color, String statusMessage) {

            public static UserResponse from(final ReadRequestFriendsDto.FriendDto.UserDto user) {
                return new UserResponse(user.id(), user.email(), user.name(), user.color(), user.statusMessage());
            }
        }
    }
}
