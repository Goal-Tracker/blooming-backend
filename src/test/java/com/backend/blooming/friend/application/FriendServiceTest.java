package com.backend.blooming.friend.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.friend.application.exception.AlreadyRequestedFriendException;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FriendServiceTest extends FriendServiceTestFixture {

    @Autowired
    FriendService friendService;

    @Test
    void 친구를_요청한다() {
        // when
        final Long actual = friendService.create(사용자_아이디, 친구_요청할_사용자_아이디);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 존재하지_않는_사용자로_친구를_요청하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> friendService.create(존재하지_않는_사용자_아이디, 친구_요청할_사용자_아이디))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 존재하지_않는_사용자를_친구로_요청하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> friendService.create(사용자_아이디, 존재하지_않는_사용자_아이디))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 이미_친구_요청을_보낸_사용자에게_다시_친구_요청할_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> friendService.create(사용자_아이디, 이미_친구_요청한_사용자))
                .isInstanceOf(AlreadyRequestedFriendException.class);
    }

    @Test
    void 이미_친구_요청이_온_사용자에게_친구_요청할_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> friendService.create(이미_친구_요청한_사용자, 사용자_아이디))
                .isInstanceOf(AlreadyRequestedFriendException.class);
    }
}
