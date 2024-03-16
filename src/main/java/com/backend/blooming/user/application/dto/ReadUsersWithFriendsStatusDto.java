package com.backend.blooming.user.application.dto;

import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.dto.UserWithFriendsStatusDto;

import java.util.List;

public record ReadUsersWithFriendsStatusDto(List<ReadUserWithFriendsStatusDto> users) {

    public static ReadUsersWithFriendsStatusDto from(final List<UserWithFriendsStatusDto> userWithFriendsStatusDtos) {
        final List<ReadUserWithFriendsStatusDto> users =
                userWithFriendsStatusDtos.stream()
                                         .map(ReadUserWithFriendsStatusDto::from)
                                         .toList();

        return new ReadUsersWithFriendsStatusDto(users);
    }

    public record ReadUserWithFriendsStatusDto(
            Long id,
            String oAuthId,
            String oAuthType,
            String email,
            String name,
            String profileImageUrl,
            String color,
            String statusMessage,
            String friendsStatus
    ) {

        public static ReadUserWithFriendsStatusDto from(final UserWithFriendsStatusDto userWithFriendsStatusDto) {
            final User user = userWithFriendsStatusDto.user();

            return new ReadUserWithFriendsStatusDto(
                    user.getId(),
                    user.getOAuthId(),
                    user.getOAuthType().name(),
                    user.getEmail(),
                    user.getName(),
                    user.getProfileImageUrl(),
                    user.getColorCode(),
                    user.getStatusMessage(),
                    userWithFriendsStatusDto.friendsStatus().name()
            );
        }
    }
}
