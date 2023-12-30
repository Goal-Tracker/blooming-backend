package com.backend.blooming.user.infrastructure.repository;

import com.backend.blooming.configuration.JpaConfiguration;
import com.backend.blooming.configuration.QuerydslConfiguration;
import com.backend.blooming.user.infrastructure.repository.dto.FriendsStatus;
import com.backend.blooming.user.infrastructure.repository.dto.UserWithFriendsStatusDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DataJpaTest
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserWithFriendsStatusRepositoryTest extends UserWithFriendsStatusRepositoryTestFixture {

    UserWithFriendsStatusRepository userWithFriendsStatusRepository;

    @BeforeEach
    void setUp(@Autowired final JPAQueryFactory jpaQueryFactory) {
        userWithFriendsStatusRepository = new UserWithFriendsStatusRepositoryImpl(jpaQueryFactory);
    }

    @Test
    void 키워드_없이_사용자_목록_조회시_모든_사용자_정보와_함께_친구_상태와_함께_반환한다() {
        // when
        final List<UserWithFriendsStatusDto> actual =
                userWithFriendsStatusRepository.findAllByNameContainsAndDeletedIsFalse(사용자, "");

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(5);
            softAssertions.assertThat(actual.get(0).user()).isEqualTo(사용자);
            softAssertions.assertThat(actual.get(0).friendsStatus()).isEqualTo(FriendsStatus.SELF);
            softAssertions.assertThat(actual.get(1).user()).isEqualTo(친구인_사용자);
            softAssertions.assertThat(actual.get(1).friendsStatus()).isEqualTo(FriendsStatus.FRIENDS);
            softAssertions.assertThat(actual.get(2).user()).isEqualTo(친구로_요청한_사용자);
            softAssertions.assertThat(actual.get(2).friendsStatus()).isEqualTo(FriendsStatus.REQUEST);
            softAssertions.assertThat(actual.get(3).user()).isEqualTo(친구_요청을_받은_사용자);
            softAssertions.assertThat(actual.get(3).friendsStatus()).isEqualTo(FriendsStatus.REQUESTED);
            softAssertions.assertThat(actual.get(4).user()).isEqualTo(관계가_없는_사용자);
            softAssertions.assertThat(actual.get(4).friendsStatus()).isEqualTo(FriendsStatus.NONE);
        });
    }

    @Test
    void 키워드를_통해_사용자_목록_조회시_이름에_키워드가_포함된_모든_사용자_정보를_반환한다() {
        // when
        final List<UserWithFriendsStatusDto> actual =
                userWithFriendsStatusRepository.findAllByNameContainsAndDeletedIsFalse(사용자, "검색어");

        // given
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).user().getName()).contains("검색어");
            softAssertions.assertThat(actual.get(1).user().getName()).contains("검색어");
        });
    }
}
