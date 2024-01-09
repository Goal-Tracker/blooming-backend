package com.backend.blooming.goal.domain;

import com.backend.blooming.goal.application.exception.InvalidGoalException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class GoalTermTest extends GoalTermTestFixture {

    @Test
    void 골_날짜수가_1_미만_100_초과인_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> new GoalTerm(예외가_발생하는_경우_시작날짜, 예외가_발생하는_경우_종료날짜))
                .isInstanceOf(InvalidGoalException.InvalidInvalidGoalDays.class);
    }

    @Test
    void 현재_골_진행_날짜수가_전체_날짜수보다_크지_않도록_한다() {
        // when
        final GoalTerm goalTerm = new GoalTerm(LocalDate.now().minusDays(30), LocalDate.now().minusDays(1));

        // then
        assertThat(goalTerm.getInProgressDays()).isEqualTo(goalTerm.getDays());
    }
}
