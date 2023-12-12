package com.backend.blooming.friend.infrastructure.repository;

import com.backend.blooming.configuration.JpaConfiguration;
import com.backend.blooming.friend.domain.Friend;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DataJpaTest
@Import(JpaConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FriendRepositoryTest extends FriendRepositoryTestFixture {

    @Autowired
    FriendRepository friendRepository;

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
    void 요청한_사용자와_요청받은_사용자가_동일한_데이터를_조회한다() {
        // when
        final Optional<Friend> actual = friendRepository.findByRequestUserIdAndRequestedUserId(
                친구_요청한_사용자.getId(),
                이미_친구_요청_받은_사용자.getId()
        );

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual).contains(친구_요청);
        });
    }

    @Test
    void 요청한_사용자와_요청받은_사용자가_동일한_데이터가_없다면_빈_optional을_반환한다() {
        // when
        final Optional<Friend> actual = friendRepository.findByRequestUserIdAndRequestedUserId(
                친구_요청한_사용자.getId(),
                친구_요청을_받은적_없는_사용자.getId()
        );

        // then
        assertThat(actual).isEmpty();
    }
}
