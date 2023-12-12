package com.backend.blooming.friend.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FriendTest extends FriendTestFixture {

    @Test
    void 친구_수락_시_친구_관계를_참으로_변경한다() {
        // given
        final Friend friend = new Friend(친구_요청을_한_사용자, 친구_요청을_받은_사용자);

        // when
        friend.acceptRequest();

        // then
        assertThat(friend.isFriends()).isTrue();
    }
}
