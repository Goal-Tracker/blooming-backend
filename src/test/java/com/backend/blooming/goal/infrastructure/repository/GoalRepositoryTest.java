package com.backend.blooming.goal.infrastructure.repository;

import com.backend.blooming.configuration.JpaConfiguration;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.goal.domain.Goal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class GoalRepositoryTest extends GoalRepositoryTestFixture {

    @Autowired
    private GoalRepository goalRepository;

    @Test
    void 요청한_골_아이디에_해당하는_골_정보를_반환한다() {
        // when
        final Goal result = goalRepository.findByIdAndDeletedIsFalse(유효한_골.getId())
                                          .orElseThrow(NotFoundGoalException::new);
        System.out.println(result.getTeams().get(0).getId());
        System.out.println(result.getTeams().get(0).getGoal());
        System.out.println(result.getTeams().get(0).getUser());
        System.out.println(result.getTeams().get(1));

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(유효한_골);
        assertThat(result.getTeams()).hasSize(2);
    }
}
