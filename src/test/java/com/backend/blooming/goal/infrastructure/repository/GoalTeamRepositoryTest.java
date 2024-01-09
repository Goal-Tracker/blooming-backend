package com.backend.blooming.goal.infrastructure.repository;

import com.backend.blooming.configuration.JpaConfiguration;
import com.backend.blooming.goal.domain.GoalTeam;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DataJpaTest
@Import(JpaConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class GoalTeamRepositoryTest extends GoalTeamRepositoryTestFixture {

    @Autowired
    private GoalTeamRepository goalTeamRepository;

    @Test
    void 해당_사용자_아이디가_참여하고_있는_모든_골_팀을_조회한다() {
        // when
        final List<GoalTeam> result = goalTeamRepository.findAllByUserId(유효한_사용자_아이디);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).hasSize(2);
            softAssertions.assertThat(result).containsAll(List.of(유효한_골_팀, 유효한_골_팀2));
        });
    }
}
