package com.backend.blooming.user.application;

import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.application.dto.UpdateUserDto;
import com.backend.blooming.user.application.dto.UserDto;
import com.backend.blooming.user.application.dto.ReadUsersWithFriendsStatusDto;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import com.backend.blooming.user.infrastructure.repository.UserWithFriendsStatusRepository;
import com.backend.blooming.user.infrastructure.repository.dto.UserWithFriendsStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserWithFriendsStatusRepository userWithFriendsStatusRepository;

    @Transactional(readOnly = true)
    public UserDto readById(final Long userId) {
        final User user = userRepository.findByIdAndDeletedIsFalse(userId)
                                        .orElseThrow(NotFoundUserException::new);

        return UserDto.from(user);
    }

    @Transactional(readOnly = true)
    public ReadUsersWithFriendsStatusDto readAllWithKeyword(final Long userId, final String keyword) {
        final User user = userRepository.findByIdAndDeletedIsFalse(userId)
                                        .orElseThrow(NotFoundUserException::new);
        final List<UserWithFriendsStatusDto> users =
                userWithFriendsStatusRepository.findAllByNameContainsAndDeletedIsFalse(user, keyword);

        return ReadUsersWithFriendsStatusDto.from(users);
    }

    public UserDto updateById(final Long userId, final UpdateUserDto updateUserDto) {
        final User user = userRepository.findById(userId)
                                        .orElseThrow(NotFoundUserException::new);
        updateUserByRequest(user, updateUserDto);
        userRepository.flush();

        return UserDto.from(user);
    }

    private void updateUserByRequest(final User user, final UpdateUserDto updateUserDto) {
        if (updateUserDto.name() != null) {
            user.updateName(updateUserDto.name());
        }
        if (updateUserDto.color() != null) {
            final ThemeColor themeColor = ThemeColor.from(updateUserDto.color());
            user.updateColor(themeColor);
        }
        if (updateUserDto.statusMessage() != null) {
            user.updateStatusMessage(updateUserDto.statusMessage());
        }
    }
}
