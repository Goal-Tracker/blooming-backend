package com.backend.blooming.user.application.dto;

import com.backend.blooming.user.domain.User;

import java.util.List;

public record UsersDto(List<UserDto> users) {

    public static UsersDto from(final List<User> users) {
        final List<UserDto> userDtos = users.stream()
                                            .map(UserDto::from)
                                            .toList();

        return new UsersDto(userDtos);
    }
}
