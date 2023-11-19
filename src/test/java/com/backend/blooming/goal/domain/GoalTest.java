package com.backend.blooming.goal.domain;

import com.backend.blooming.goal.application.GoalServiceFixture;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GoalTest extends GoalServiceFixture {

    final String goalName = "제목";
    final String goalMemo = "골 메모";

    @Test
    void 골_시작날짜가_유효하지_않은_경우_예외처리() {
        // given
        final String goalStartDay = "2023-11-18";
        final String goalEndDay = "2023-11-30";

        // when, then
        assertThatThrownBy(() -> Goal.builder()
                .goalName(goalName)
                .goalMemo(goalMemo)
                .goalStartDay(goalStartDay)
                .goalEndDay(goalEndDay)
                .goalDays(1)
                .goalTeams(goalTeams)
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 골_종료날짜가_유효하지_않은_경우_예외처리() {
        // given
        final String goalStartDay = "2023-11-17";
        final String goalEndDay = "2023-11-18";

        // when, then
        assertThatThrownBy(() -> Goal.builder()
                .goalName(goalName)
                .goalMemo(goalMemo)
                .goalStartDay(goalStartDay)
                .goalEndDay(goalEndDay)
                .goalDays(1)
                .goalTeams(goalTeams)
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 골이_유효하지_않은_경우_예외처리() {
        // given
        final String goalStartDay = "2023-11-21";
        final String goalEndDay = "2023-11-20";

        // when, then
        assertThatThrownBy(() -> Goal.builder()
                .goalName(goalName)
                .goalMemo(goalMemo)
                .goalStartDay(goalStartDay)
                .goalEndDay(goalEndDay)
                .goalDays(1)
                .goalTeams(goalTeams)
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 골_날짜가_유효하지_않은_경우_예외처리() {
        // given
        final String goalStartDay = "2023-11-20";
        final String goalEndDay = "2023-11-20";

        // when, then
        assertThatThrownBy(() -> Goal.builder()
                .goalName(goalName)
                .goalMemo(goalMemo)
                .goalStartDay(goalStartDay)
                .goalEndDay(goalEndDay)
                .goalDays(0)
                .goalTeams(goalTeams)
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 골_팀이_유효하지_않은_경우_예외처리(){
        // given
        final String goalStartDay = "2023-11-20";
        final String goalEndDay = "2023-11-30";
        final List<GoalTeam> unavailableGoalTeams = new ArrayList<>();

        // when, then
        assertThatThrownBy(() -> Goal.builder()
                .goalName(goalName)
                .goalMemo(goalMemo)
                .goalStartDay(goalStartDay)
                .goalEndDay(goalEndDay)
                .goalDays(11)
                .goalTeams(unavailableGoalTeams)
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }
}