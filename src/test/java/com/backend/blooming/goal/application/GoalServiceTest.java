package com.backend.blooming.goal.application;

import com.backend.blooming.goal.application.dto.GoalDto;
import com.backend.blooming.goal.application.exception.GoalException;
import com.backend.blooming.goal.domain.GoalTeam;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class GoalServiceTest extends GoalServiceTestFixture {

    @Autowired
    private GoalService goalService;

    @Test
    public void 골을_생성한다() {
        // when
        final GoalDto result = goalService.createGoal(유효한_골_생성_dto);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).isNotNull();
            softAssertions.assertThat(result.goalName()).isEqualTo(골_제목);
            softAssertions.assertThat(result.goalMemo()).isEqualTo(골_메모);
            softAssertions.assertThat(result.goalStartDay()).isEqualTo(골_시작일);
            softAssertions.assertThat(result.goalEndDay()).isEqualTo(골_종료일);
            softAssertions.assertThat(result.goalDays()).isEqualTo(골_날짜수);
            softAssertions.assertThat(result.goalTeamUserIds()).isEqualTo(골_팀에_등록된_사용자_아이디_목록);
        });
    }

    @Test
    public void 골_팀을_생성한다() {
        // when
        final List<GoalTeam> result = goalService.createGoalTeams(골_팀에_등록된_사용자_아이디_목록, 골_아이디);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).isNotEmpty();
            softAssertions.assertThat(result.get(0).getUser().getId()).isEqualTo(유효한_사용자_아이디);
        });
    }

    @Test
    void 골_시작날짜가_현재보다_이전인_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> goalService.createGoal(골_시작날짜가_현재보다_이전인_골_생성_dto))
                .isInstanceOf(GoalException.InvalidGoalStartDay.class);
    }

    @Test
    void 골_종료날짜가_현재보다_이전인_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> goalService.createGoal(골_종료날짜가_현재보다_이전인_골_생성_dto))
                .isInstanceOf(GoalException.InvalidGoalEndDay.class);
    }

    @Test
    void 골_종료날짜가_시작날짜보다_이전인_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> goalService.createGoal(골_종료날짜가_시작날짜보다_이전인_골_생성_dto))
                .isInstanceOf(GoalException.InvalidGoalPeriod.class);
    }

    @Test
    void 골_날짜가_1_미만인_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> goalService.createGoal(골_날짜가_1_미만인_골_생성_dto))
                .isInstanceOf(GoalException.InvalidGoalDays.class);
    }
}
