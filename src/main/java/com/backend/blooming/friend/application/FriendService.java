package com.backend.blooming.friend.application;

import com.backend.blooming.friend.application.exception.AlreadyRequestedFriendException;
import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.friend.infrastructure.repository.FriendRepository;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public Long create(final Long userId, final Long friendId) {
        validateFriendStatus(userId, friendId);

        final User user = findUser(userId);
        final User friendUser = findUser(friendId);
        final Friend friend = new Friend(user, friendUser);

        return friendRepository.save(friend)
                               .getId();
    }

    private void validateFriendStatus(final Long userId, final Long friendId) {
        if (friendRepository.existsByRequestFriend(userId, friendId)) {
            throw new AlreadyRequestedFriendException();
        }
    }

    private User findUser(final Long userId) {
        return userRepository.findByIdAndDeletedIsFalse(userId)
                             .orElseThrow(NotFoundUserException::new);
    }
}
