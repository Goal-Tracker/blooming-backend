package com.backend.blooming.user.presentation.dto.response;

import com.backend.blooming.user.application.dto.ReadUsersWithFriendsStatusDto;

import java.util.List;

import static com.backend.blooming.user.application.dto.ReadUsersWithFriendsStatusDto.ReadUserWithFriendsStatusDto;

public record ReadUsersWithFriendsStatusResponse(List<ReadUserWithFriendsStatusResponse> users) {

    public static ReadUsersWithFriendsStatusResponse from(final ReadUsersWithFriendsStatusDto usersDto) {
        final List<ReadUserWithFriendsStatusResponse> users = usersDto.users()
                                                                      .stream()
                                                                      .map(ReadUserWithFriendsStatusResponse::from)
                                                                      .toList();

        return new ReadUsersWithFriendsStatusResponse(users);
    }

    public record ReadUserWithFriendsStatusResponse(
            Long id,
            String email,
            String name,
            String profileImageUrl,
            String color,
            String statusMessage,
            String friendsStatus
    ) {

        public static ReadUserWithFriendsStatusResponse from(final ReadUserWithFriendsStatusDto userDto) {
            return new ReadUserWithFriendsStatusResponse(
                    userDto.id(),
                    userDto.email(),
                    userDto.name(),
                    userDto.profileImageUrl(),
                    userDto.color(),
                    userDto.statusMessage(),
                    userDto.friendsStatus()
            );
        }
    }
}
