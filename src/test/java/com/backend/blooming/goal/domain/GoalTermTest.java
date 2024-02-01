package com.backend.blooming.goal.domain;

import com.backend.blooming.goal.application.exception.InvalidGoalException;
import com.backend.blooming.goal.application.exception.UpdateGoalForbiddenException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class GoalTermTest extends GoalTermTestFixture {

    @Test
    void 골_시작날짜가_현재보다_이전인_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> new GoalTerm(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2)))
                .isInstanceOf(InvalidGoalException.InvalidInvalidGoalStartDay.class);
    }

    @Test
    void 골_종료날짜가_시작날짜보다_이전인_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> new GoalTerm(LocalDate.now().plusDays(5), LocalDate.now().plusDays(2)))
                .isInstanceOf(InvalidGoalException.InvalidInvalidGoalPeriod.class);
    }

    @Test
    void 골_날짜가_100_초과인_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> new GoalTerm(LocalDate.now(), LocalDate.now().plusDays(100)))
                .isInstanceOf(InvalidGoalException.InvalidInvalidGoalDays.class);
    }

    @Test
    void 수정할_종료날자가_현재_종료날짜보다_이전인_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> 수정_전_골_기간.updateEndDate(LocalDate.now().plusDays(30)))
                .isInstanceOf(UpdateGoalForbiddenException.ForbiddenEndDateToUpdate.class);
    }
}
