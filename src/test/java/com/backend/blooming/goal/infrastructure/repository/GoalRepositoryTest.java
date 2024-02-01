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

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

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

    @Test
    void 요청한_사용자_아이디가_골_참여자로_있는_골_중_현재_진행중인_모든_골을_반환한다() {
        // when
        final List<Goal> result = goalRepository.findAllByUserIdAndInProgress(골_관리자_사용자.getId(), LocalDate.now().plusDays(테스트를_위한_시스템_현재_시간_설정값));

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).hasSize(사용자가_참여한_골_중_현재_진행중인_골_목록.size());
            softAssertions.assertThat(result.get(0).getId()).isEqualTo(유효한_골.getId());
            softAssertions.assertThat(result.get(0).getName()).isEqualTo(유효한_골.getName());
            softAssertions.assertThat(result.get(1).getId()).isEqualTo(현재_진행중인_골.getId());
            softAssertions.assertThat(result.get(1).getName()).isEqualTo(현재_진행중인_골.getName());
        });
    }

    @Test
    void 요청한_사용자_아이디가_골_참여자로_있는_골_중_종료된_모든_골을_반환한다() {
        // when
        final List<Goal> result = goalRepository.findAllByUserIdAndFinished(골_관리자_사용자.getId(), LocalDate.now().plusDays(테스트를_위한_시스템_현재_시간_설정값));

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).hasSize(사용자가_참여한_골_중_종료된_골_목록.size());
            softAssertions.assertThat(result.get(0).getId()).isEqualTo(이미_종료된_골.getId());
            softAssertions.assertThat(result.get(0).getName()).isEqualTo(이미_종료된_골.getName());
            softAssertions.assertThat(result.get(1).getId()).isEqualTo(이미_종료된_골2.getId());
            softAssertions.assertThat(result.get(1).getName()).isEqualTo(이미_종료된_골2.getName());
        });
    }
}
