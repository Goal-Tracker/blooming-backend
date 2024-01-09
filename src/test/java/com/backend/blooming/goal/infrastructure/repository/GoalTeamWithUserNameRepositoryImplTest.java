package com.backend.blooming.goal.infrastructure.repository;

import com.backend.blooming.configuration.JpaConfiguration;
import com.backend.blooming.configuration.QuerydslConfiguration;
import com.backend.blooming.goal.infrastructure.repository.dto.GoalTeamWithUserNameDto;
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
class GoalTeamWithUserNameRepositoryTest extends GoalTeamWithUserNameRepositoryTestFixture {

    private GoalTeamWithUserNameRepository goalTeamWithUserNameRepository;

    @BeforeEach
    void setUp(@Autowired final JPAQueryFactory jpaQueryFactory) {
        goalTeamWithUserNameRepository = new GoalTeamWithUserNameRepositoryImpl(jpaQueryFactory);
    }

    @Test
    void 해당_골_아이디에_참여하는_모든_사용자의_정보를_조회한다() {
        // when
        final List<GoalTeamWithUserNameDto> result = goalTeamWithUserNameRepository.findAllByGoalIdAndDeletedIsFalse(참여한_골_아이디);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).hasSize(2);
            softAssertions.assertThat(result).containsAll(List.of(골에_참여한_사용자_dto_1, 골에_참여한_사용자_dto_2));
        });
    }
}
