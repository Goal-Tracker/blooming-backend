package com.backend.blooming.goal.application;

import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
class GoalServiceTest extends GoalServiceFixture {

    @Autowired
    private GoalService goalService;

    @Test
    public void 골을_생성한다(){
        // when
        final Goal result = goalService.persistGoal(유효한_골_생성_dto);
        final List<GoalTeam> goalTeamsResult = goalService.createGoalTeams(골_팀에_등록된_사용자_아이디_목록, result);
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
    public void 골_팀을_생성한다(){
        // when
        final List<GoalTeam> result = goalService.createGoalTeams(골_팀에_등록된_사용자_아이디_목록, 유효한_골);

        // then
        assertThat(result.get(0).getUser().getId()).isEqualTo(유효한_사용자_아이디);
    }
}
