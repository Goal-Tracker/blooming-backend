package com.backend.blooming.goal.application;

import com.backend.blooming.goal.application.fixture.GoalServiceFixture;
import com.backend.blooming.goal.domain.Goal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;

class GoalServiceTest extends GoalServiceFixture {

    @Autowired
    private GoalService goalService;

    private final String goalName = "제목";
    private final String goalMemo = "내용";
    private final String goalStartDay = "2023-11-05";
    private final String goalEndDay = "2024-01-03";
    private final int goalDays = 60;

    @Test
    public void 골_등록_성공() throws ParseException {
        // when
        final Goal result = goalService.createGoal(유효한_골_생성_dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getGoalName()).isEqualTo(goalName);
        assertThat(result.getGoalMemo()).isEqualTo(goalMemo);
        assertThat(result.getGoalStartDay()).isEqualTo(goalStartDay);
        assertThat(result.getGoalEndDay()).isEqualTo(goalEndDay);
        assertThat(result.getGoalDays()).isEqualTo(goalDays);
    }

}