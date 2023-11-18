package com.backend.blooming.user.application;

import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.application.dto.UpdateUserDto;
import com.backend.blooming.user.application.dto.UserDto;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserDto readById(final Long userId) {
        final User user = userRepository.findByIdAndDeletedIsFalse(userId)
                                        .orElseThrow(() -> new NotFoundUserException("사용자를 조회할 수 없습니다."));

        return UserDto.from(user);
    }

    public UserDto updateById(final Long userId, final UpdateUserDto updateUserDto) {
        final User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new NotFoundUserException("사용자를 조회할 수 없습니다."));
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
