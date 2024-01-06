package com.backend.blooming.friend.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.friend.application.dto.ReadFriendsDto;
import com.backend.blooming.friend.application.exception.AlreadyRequestedFriendException;
import com.backend.blooming.friend.application.exception.DeleteFriendForbiddenException;
import com.backend.blooming.friend.application.exception.FriendAcceptanceForbiddenException;
import com.backend.blooming.friend.application.exception.NotFoundFriendRequestException;
import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.friend.infrastructure.repository.FriendRepository;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FriendServiceTest extends FriendServiceTestFixture {

    @Autowired
    private FriendService friendService;

    @Autowired
    private FriendRepository friendRepository;

    @Test
    void 친구를_요청한다() {
        // when
        final Long actual = friendService.request(사용자_아이디, 아직_친구_요청_전의_사용자_아이디);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 존재하지_않는_사용자로_친구를_요청하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> friendService.request(존재하지_않는_사용자_아이디, 아직_친구_요청_전의_사용자_아이디))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 존재하지_않는_사용자를_친구로_요청하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> friendService.request(사용자_아이디, 존재하지_않는_사용자_아이디))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 이미_친구_요청을_보낸_사용자에게_다시_친구_요청할_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> friendService.request(사용자_아이디, 이미_친구_요청을_받은_사용자_아이디))
                .isInstanceOf(AlreadyRequestedFriendException.class);
    }

    @Test
    void 이미_친구_요청이_온_사용자에게_친구_요청할_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> friendService.request(이미_친구_요청을_받은_사용자_아이디, 사용자_아이디))
                .isInstanceOf(AlreadyRequestedFriendException.class);
    }

    @Test
    void 자신이_친구_요청한_사용자_목록을_조회한다() {
        // when
        final ReadFriendsDto actual = friendService.readAllByRequestId(친구_요청을_보낸_사용자_아이디);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.friends()).hasSize(3);
            softAssertions.assertThat(actual.friends().get(0)).isEqualTo(친구_요청_사용자_정보_dto1);
            softAssertions.assertThat(actual.friends().get(1)).isEqualTo(친구_요청_사용자_정보_dto2);
            softAssertions.assertThat(actual.friends().get(2)).isEqualTo(친구_요청_사용자_정보_dto3);
        });
    }

    @Test
    void 자신에게_친구_요청을_받은_사용자_목록을_조회한다() {
        // when
        final ReadFriendsDto actual = friendService.readAllByRequestedId(친구_요청을_받은_사용자_아이디);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.friends()).hasSize(3);
            softAssertions.assertThat(actual.friends().get(0)).isEqualTo(친구_요청을_받은_사용자_정보_dto1);
            softAssertions.assertThat(actual.friends().get(1)).isEqualTo(친구_요청을_받은_사용자_정보_dto2);
            softAssertions.assertThat(actual.friends().get(2)).isEqualTo(친구_요청을_받은_사용자_정보_dto3);
        });
    }

    @Test
    void 서로_친구인_사용자_목록을_조회한다() {
        // when
        final ReadFriendsDto actual = friendService.readAllMutualByUserId(사용자_아이디);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.friends()).hasSize(3);
            softAssertions.assertThat(actual.friends().get(0)).isEqualTo(서로_친구인_사용자_정보_dto1);
            softAssertions.assertThat(actual.friends().get(1)).isEqualTo(서로_친구인_사용자_정보_dto2);
            softAssertions.assertThat(actual.friends().get(2)).isEqualTo(서로_친구인_사용자_정보_dto3);
        });
    }

    @Test
    void 친구_요청을_수락한다() {
        // when
        friendService.accept(이미_친구_요청을_받은_사용자_아이디, 친구_요청_아이디);

        // then
        final Optional<Friend> actual = friendRepository.findById(친구_요청_아이디);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get().isFriends()).isTrue();
        });
    }

    @Test
    void 친구_요청_수락시_해당_요청이_존재하지_않는_경우_예외를_반환한다() {
        // when & then
        assertThatThrownBy(() -> friendService.accept(사용자_아이디, 존재하지_않는_친구_요청_아이디))
                .isInstanceOf(NotFoundFriendRequestException.class);
    }

    @Test
    void 친구_요청_수락시_사용자가_해당_요청의_받는이가_아닌_경우_예외를_반환한다() {
        // when & then
        assertThatThrownBy(() -> friendService.accept(친구_요청을_받지_않은_사용자_아이디, 친구_요청_아이디))
                .isInstanceOf(FriendAcceptanceForbiddenException.class);
    }

    @Test
    void 친구_요청을_삭제한다() {
        // when
        friendService.delete(사용자_아이디, 친구_요청_아이디);

        // then
        final Optional<Friend> actual = friendRepository.findById(친구_요청_아이디);
        assertThat(actual).isEmpty();
    }

    @Test
    void 친구_요청_삭제시_존재하지_않는_사용자라면_예외를_반환한다() {
        // when & then
        assertThatThrownBy(() -> friendService.delete(존재하지_않는_사용자_아이디, 친구_요청_아이디))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 친구_요쳥_삭제시_존재하지_않는_요청이라면_예외를_반환한다() {
        // when & then
        assertThatThrownBy(() -> friendService.delete(사용자_아이디, 존재하지_않는_친구_요청_아이디))
                .isInstanceOf(NotFoundFriendRequestException.class);
    }

    @Test
    void 친구_요청_삭제시_요청하거나_요청_받은_사용자가_아니라면_예외를_반환한다() {
        // when & then
        assertThatThrownBy(() -> friendService.delete(친구_요청을_받지_않은_사용자_아이디, 친구_요청_아이디))
                .isInstanceOf(DeleteFriendForbiddenException.class);
    }
}
