package com.backend.blooming.goal.application;

import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.exception.GoalException;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
class GoalServiceTest extends GoalServiceFixture {

    @Autowired
    private GoalService goalService;

    @Test
    public void 골을_생성한다() {
        // when
        final Goal result = goalService.persistGoal(유효한_골_생성_dto);
        final List<GoalTeam> goalTeamsResult = goalService.createGoalTeams(골_팀에_등록된_사용자_아이디_목록, result.getId());
        result.updateGoalTeams(goalTeamsResult);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getGoalName()).isEqualTo(골_제목);
        assertThat(result.getGoalMemo()).isEqualTo(골_메모);
        assertThat(result.getGoalStartDay()).isEqualTo(골_시작일);
        assertThat(result.getGoalEndDay()).isEqualTo(골_종료일);
        assertThat(result.getGoalDays()).isEqualTo(골_날짜수);
        assertThat(result.getGoalTeamIds()).isEqualTo(골_팀에_등록된_사용자_아이디_목록);
    }

    @Test
    public void 골_팀을_생성한다() {
        // when
        final List<GoalTeam> result = goalService.createGoalTeams(골_팀에_등록된_사용자_아이디_목록, 유효한_골.getId());

        // then
        assertThat(result.get(0).getUser().getId()).isEqualTo(유효한_사용자_아이디);
    }

    @Test
    void 골_시작날짜가_현재보다_이전인_경우_예외를_발생한다() {
        // given
        final CreateGoalDto goalForStartDayException = new CreateGoalDto(
                골_아이디,
                골_제목,
                골_메모,
                "2023-11-18",
                골_종료일,
                골_날짜수,
                골_팀에_등록된_사용자_아이디_목록
        );

        // when, then
        assertThatThrownBy(() -> goalService.persistGoal(goalForStartDayException))
                .isInstanceOf(GoalException.InvalidGoalStartDay.class);
    }

    @Test
    void 골_종료날짜가_현재보다_이전인_경우_예외를_발생한다() {
        // given
        final CreateGoalDto goalForEndDayException = new CreateGoalDto(
                골_아이디,
                골_제목,
                골_메모,
                골_시작일,
                "2023-11-18",
                골_날짜수,
                골_팀에_등록된_사용자_아이디_목록
        );

        // when, then
        assertThatThrownBy(() -> goalService.persistGoal(goalForEndDayException))
                .isInstanceOf(GoalException.InvalidGoalEndDay.class);
    }

    @Test
    void 골_종료날짜가_시작날짜보다_이전인_경우_예외를_발생한다() {
        // given
        final CreateGoalDto goalForPeriodException = new CreateGoalDto(
                골_아이디,
                골_제목,
                골_메모,
                "2023-11-30",
                "2023-11-28",
                골_날짜수,
                골_팀에_등록된_사용자_아이디_목록
        );

        // when, then
        assertThatThrownBy(() -> goalService.persistGoal(goalForPeriodException))
                .isInstanceOf(GoalException.InvalidGoalPeriod.class);
    }

    @Test
    void 골_날짜가_유효하지_않은_경우_예외처리() {
        // given
        final CreateGoalDto goalForDaysException = new CreateGoalDto(
                골_아이디,
                골_제목,
                골_메모,
                골_시작일,
                골_종료일,
                0,
                골_팀에_등록된_사용자_아이디_목록
        );

        // when, then
        assertThatThrownBy(() -> goalService.persistGoal(goalForDaysException))
                .isInstanceOf(GoalException.InvalidGoalDays.class);
    }
}
