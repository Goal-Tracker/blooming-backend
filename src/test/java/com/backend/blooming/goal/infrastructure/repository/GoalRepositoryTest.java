package com.backend.blooming.goal.infrastructure.repository;

import com.backend.blooming.goal.application.GoalServiceFixture;
import com.backend.blooming.goal.domain.Goal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class GoalRepositoryTest extends GoalServiceFixture {

    @Autowired
    private GoalRepository goalRepository;

    @Test
    void 골_등록() throws ParseException {
        // given
        final Goal goal = Goal.builder()
                .goalName("7시 기상")
                .goalMemo("일어나라")
                .goalStartDay("2023-11-05")
                .goalEndDay("2024-01-03")
                .goalDays(60)
                .goalTeams(goalTeams)
                .build();

        // when
        final Goal result = goalRepository.save(goal);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(goal);
    }

    @Test
    void 골이_존재하는지_테스트() throws ParseException {
        // given
        final Goal goal = Goal.builder()
                .goalName("7시 기상")
                .goalMemo("일어나라")
                .goalStartDay("2023-11-05")
                .goalEndDay("2024-01-03")
                .goalDays(60)
                .goalTeams(goalTeams)
                .build();

        // when
        goalRepository.save(goal);
        final Goal findResult = goalRepository.findById(goal.getId()).get();

        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult.getId()).isNotNull();
        assertThat(findResult).usingRecursiveComparison().isEqualTo(goal);
    }
}
