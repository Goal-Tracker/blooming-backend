package com.backend.blooming.friend.domain;

import com.backend.blooming.user.domain.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FriendTest extends FriendTestFixture {

    @Test
    void 친구_수락시_친구_관계를_참으로_변경한다() {
        // given
        final Friend friend = new Friend(친구_요청을_한_사용자, 친구_요청을_받은_사용자);

        // when
        friend.acceptRequest();

        // then
        assertThat(friend.isFriends()).isTrue();
    }

    @Test
    void 친구_요청_받은_사용자인지_확인시_친구_요청을_받은_사용자라면_참을_반환한다() {
        // given
        final Friend friend = new Friend(친구_요청을_한_사용자, 친구_요청을_받은_사용자);

        // when
        final boolean actual = friend.isRequestedUser(친구_요청을_받은_사용자);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 친구_요청_받은_사용자인지_확인시_친구_요청을_받은_사용자가_아니라면_거짓을_반환한다() {
        // given
        final Friend friend = new Friend(친구_요청을_한_사용자, 친구_요청을_받은_사용자);

        // when
        final boolean actual = friend.isRequestedUser(친구_요청을_한_사용자);

        // then
        assertThat(actual).isFalse();
    }

    @ParameterizedTest
    @MethodSource("provideUsers")
    void 친구_관계의_사용자_중_한_명인지_확인시_요청자_혹은_요청을_받은_사용자라면_참을_반환한다(final User user) {
        // given
        final Friend friend = new Friend(친구_요청을_한_사용자, 친구_요청을_받은_사용자);

        // when
        final boolean actual = friend.isOneOfFriends(user);

        // then
        assertThat(actual).isTrue();
    }

    static Stream<User> provideUsers() {
        return Stream.of(친구_요청을_한_사용자, 친구_요청을_받은_사용자);
    }

    @Test
    void 친구_관계의_사용자_중_한_명인지_확인시_둘다_아니라면_거짓을_반환한다() {
        // given
        final Friend friend = new Friend(친구_요청을_한_사용자, 친구_요청을_받은_사용자);

        // when
        final boolean actual = friend.isOneOfFriends(친구_요청과_상관없는_사용자);

        // then
        assertThat(actual).isFalse();
    }
}
