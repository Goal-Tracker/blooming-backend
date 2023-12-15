package com.backend.blooming.friend.presentation.response;

import com.backend.blooming.friend.application.dto.ReadRequestedFriendsDto;

import java.util.List;

public record ReadRequestedFriendsResponse(List<FriendResponse> friends) {

    public static ReadRequestedFriendsResponse from(final ReadRequestedFriendsDto friendsDto) {
        final List<FriendResponse> friendResponses = friendsDto.friends()
                                                               .stream()
                                                               .map(FriendResponse::from)
                                                               .toList();

        return new ReadRequestedFriendsResponse(friendResponses);
    }

    public record FriendResponse(Long id, UserResponse friend, boolean isFriends) {

        public static FriendResponse from(final ReadRequestedFriendsDto.FriendDto friendDto) {
            return new FriendResponse(
                    friendDto.id(),
                    UserResponse.from(friendDto.friend()),
                    friendDto.isFriends()
            );
        }

        public record UserResponse(Long id, String email, String name, String color, String statusMessage) {

            public static UserResponse from(final ReadRequestedFriendsDto.FriendDto.UserDto user) {
                return new UserResponse(user.id(), user.email(), user.name(), user.color(), user.statusMessage());
            }
        }
    }
}
