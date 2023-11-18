package com.backend.blooming.user.application;

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
}
