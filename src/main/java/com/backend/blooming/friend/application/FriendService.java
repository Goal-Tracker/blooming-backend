package com.backend.blooming.friend.application;

import com.backend.blooming.friend.application.dto.FriendType;
import com.backend.blooming.friend.application.dto.ReadFriendsDto;
import com.backend.blooming.friend.application.exception.DeleteFriendForbiddenException;
import com.backend.blooming.friend.application.exception.FriendAcceptanceForbiddenException;
import com.backend.blooming.friend.application.exception.FriendRequestNotAllowedException;
import com.backend.blooming.friend.application.exception.NotFoundFriendRequestException;
import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.friend.infrastructure.repository.FriendRepository;
import com.backend.blooming.notification.application.NotificationService;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public Long request(final Long userId, final Long friendId) {
        validateRequestFriend(userId, friendId);

        final User user = getUser(userId);
        final User friendUser = getUser(friendId);
        final Friend friend = new Friend(user, friendUser);
        friendRepository.save(friend);

        notificationService.sendRequestFriendNotification(friend);

        return friend.getId();
    }

    private void validateRequestFriend(final Long userId, final Long friendId) {
        if (userId.equals(friendId)) {
            throw new FriendRequestNotAllowedException.SelfRequestNotAllowedException();
        }
        if (friendRepository.existsByRequestFriend(userId, friendId)) {
            throw new FriendRequestNotAllowedException.AlreadyRequestedFriendException();
        }
    }

    private User getUser(final Long userId) {
        return userRepository.findByIdAndDeletedIsFalse(userId)
                             .orElseThrow(NotFoundUserException::new);
    }

    @Transactional(readOnly = true)
    public ReadFriendsDto readAllByRequestId(final Long userId) {
        final User user = getUser(userId);
        final List<Friend> requestUsers = friendRepository.findAllByRequestUserId(userId);

        return ReadFriendsDto.of(requestUsers, user, FriendType.REQUEST);
    }

    @Transactional(readOnly = true)
    public ReadFriendsDto readAllByRequestedId(final Long userId) {
        final User user = getUser(userId);
        final List<Friend> requestedUser = friendRepository.findAllByRequestedUserId(userId);

        return ReadFriendsDto.of(requestedUser, user, FriendType.REQUESTED);
    }

    @Transactional(readOnly = true)
    public ReadFriendsDto readAllMutualByUserId(final Long userId) {
        final User user = getUser(userId);
        final List<Friend> friends = friendRepository.findAllByUserIdAndIsFriends(userId);

        return ReadFriendsDto.of(friends, user, FriendType.FRIENDS);
    }

    public void accept(final Long userId, final Long requestId) {
        final User user = getUser(userId);
        final Friend friend = getFriend(requestId);
        validateRequestedUser(user, friend);

        friend.acceptRequest();
        notificationService.sendAcceptFriendNotification(friend);
    }

    private Friend getFriend(final Long requestId) {
        return friendRepository.findById(requestId)
                               .orElseThrow(NotFoundFriendRequestException::new);
    }

    private void validateRequestedUser(final User user, final Friend friend) {
        if (!friend.isRequestedUser(user)) {
            throw new FriendAcceptanceForbiddenException();
        }
    }

    public void delete(final Long userId, final Long requestId) {
        final User user = getUser(userId);
        final Friend friend = getFriend(requestId);
        validateCanDelete(user, friend);

        friendRepository.delete(friend);
    }

    private void validateCanDelete(final User user, final Friend friend) {
        if (!friend.isOneOfFriends(user)) {
            throw new DeleteFriendForbiddenException();
        }
    }
}
