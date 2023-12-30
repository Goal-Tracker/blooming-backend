package com.backend.blooming.friend.infrastructure.repository;

import com.backend.blooming.configuration.JpaConfiguration;
import com.backend.blooming.friend.domain.Friend;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DataJpaTest
@Import(JpaConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FriendRepositoryTest extends FriendRepositoryTestFixture {

    @Autowired
    private FriendRepository friendRepository;

    @Test
    void 요청한_사용자와_요청받은_사용자가_동일한_데이터가_있다면_참을_반환한다() {
        // when
        final boolean actual = friendRepository.existsByRequestFriend(
                친구_요청한_사용자.getId(),
                이미_친구_요청_받은_사용자.getId()
        );

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 요청한_사용자와_요청받은_사용자가_반대인_데이터가_있다면_참을_반환한다() {
        // when
        final boolean actual = friendRepository.existsByRequestFriend(
                이미_친구_요청_받은_사용자.getId(),
                친구_요청한_사용자.getId()
        );

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 요청한_사용자와_요청받은_사용자가_동일한_데이터가_없다면_거짓을_반환한다() {
        // when
        final boolean actual = friendRepository.existsByRequestFriend(
                친구_요청한_사용자.getId(),
                친구_요청을_받은적_없는_사용자.getId()
        );

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 해당_사용자가_친구_요청한_모든_친구_요청을_조회한다() {
        // when
        final List<Friend> actual = friendRepository.findAllByRequestUserId(친구_요청한_사용자.getId());

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual).containsAll(List.of(보낸_친구_요청1, 보낸_친구_요청2, 보낸_친구_요청3));
        });
    }

    @Test
    void 해당_사용자에게_친구_요청한_모든_친구_요청을_조회한다() {
        // when
        final List<Friend> actual = friendRepository.findAllByRequestedUserId(친구_요청을_받은_사용자.getId());

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual).containsAll(List.of(받은_친구_요청1, 받은_친구_요청2, 받은_친구_요청3));
        });
    }

    @Test
    void 받거나_요청한_친구_요청중_친구인지_여부가_참인_모든_친구_요청을_조회한다() {
        // when
        final List<Friend> actual = friendRepository.findAllByUserIdAndIsFriends(친구가_있는_사용자.getId());

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual).containsAll(List.of(친구인_요청1, 친구인_요청2, 친구인_요청3));
        });
    }
}
