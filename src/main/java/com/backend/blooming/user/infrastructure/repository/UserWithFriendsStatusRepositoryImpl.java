package com.backend.blooming.user.infrastructure.repository;

import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.dto.QUserWithFriendsStatusQueryProjectionDto;
import com.backend.blooming.user.infrastructure.repository.dto.UserWithFriendsStatusDto;
import com.backend.blooming.user.infrastructure.repository.dto.UserWithFriendsStatusQueryProjectionDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.backend.blooming.friend.domain.QFriend.friend;
import static com.backend.blooming.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserWithFriendsStatusRepositoryImpl implements UserWithFriendsStatusRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserWithFriendsStatusDto> findAllByNameContainsAndDeletedIsFalse(
            final User currentUser,
            final String keyword
    ) {
        final List<UserWithFriendsStatusQueryProjectionDto> userWithFriendDtos =
                queryFactory.select(new QUserWithFriendsStatusQueryProjectionDto(user, friend))
                            .from(user)
                            .leftJoin(friend)
                            .on(buildFriendsConditions(currentUser.getId()))
                            .fetchJoin()
                            .where(user.deleted.isFalse().and(user.name.value.contains(keyword)))
                            .fetch();

        return userWithFriendDtos.stream()
                                 .map(userDto -> UserWithFriendsStatusDto.of(currentUser, userDto.user(), userDto.friend()))
                                 .toList();
    }

    private static BooleanExpression buildFriendsConditions(final Long currentUserId) {
        final BooleanExpression requestedFriendCondition = friend.requestUser.id.eq(currentUserId)
                                                                                .and(friend.requestedUser.id.eq(user.id));
        final BooleanExpression requestFriendCondition = friend.requestedUser.id.eq(currentUserId)
                                                                                .and(friend.requestUser.id.eq(user.id));

        return (requestedFriendCondition.or(requestFriendCondition)).and(user.deleted.isFalse());
    }
}
