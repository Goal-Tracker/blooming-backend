package com.backend.blooming.goal.domain;

import com.backend.blooming.goal.application.exception.InvalidGoalException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TeamsTest extends TeamsTestFixture {
    
    @Test
    void 골_팀_목록을_생성한다() {
        // when
        final Teams teams = Teams.create(골_참여_사용자_목록, 유효한_골);
        
        // then
        assertThat(teams.getGoalTeams()).hasSize(2);
    }
    
    @Test
    void 골_팀_목록_생성시_참여자_목록_크기가_5보다_큰_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> Teams.create(크기가_5보다_큰_골_참여_사용자_목록, 유효한_골))
                .isInstanceOf(InvalidGoalException.InvalidInvalidUsersSize.class);
    }

    @Test
    void 골_팀_목록을_수정한다() {
        // given
        final Teams teams = Teams.create(골_참여_사용자_목록, 유효한_골);

        // when
        teams.update(수정할_골_참여_사용자_목록, 유효한_골);

        // then
        assertSoftly(softAssertions -> {
            final List<GoalTeam> result = teams.getGoalTeams();
            softAssertions.assertThat(result).hasSize(3);
            assertThat(result.get(0).getUser().getId()).isEqualTo(골_참여자.getId());
            assertThat(result.get(0).getUser().getName()).isEqualTo(골_참여자.getName());
            assertThat(result.get(1).getUser().getId()).isEqualTo(골_참여자2.getId());
            assertThat(result.get(1).getUser().getName()).isEqualTo(골_참여자2.getName());
            assertThat(result.get(2).getUser().getId()).isEqualTo(추가된_골_참여자.getId());
            assertThat(result.get(2).getUser().getName()).isEqualTo(추가된_골_참여자.getName());
        });
    }
    
    @Test
    void 골_팀_목록_수정시_참여자_목록_크기가_5보다_큰_경우_예외를_발생한다() {
        // given
        final Teams teams = Teams.create(골_참여_사용자_목록, 유효한_골);
        
        // when & then
        assertThatThrownBy(() -> teams.update(크기가_5보다_큰_골_참여_사용자_목록, 유효한_골))
                .isInstanceOf(InvalidGoalException.InvalidInvalidUsersSize.class);
    }
}
