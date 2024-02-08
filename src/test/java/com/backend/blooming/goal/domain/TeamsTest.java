package com.backend.blooming.goal.domain;

import com.backend.blooming.goal.application.exception.InvalidGoalException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TeamsTest extends TeamsTestFixture {
    
    @Test
    void 골_팀_목록을_생성한다() {
        // when
        final Teams teams = new Teams(골_참여_사용자_목록, 유효한_골);
        
        // then
        assertThat(teams.getGoalTeams()).hasSize(2);
    }
    
    @Test
    void 골_팀_목록_생성시_참여자_목록_크기가_5보다_큰_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> new Teams(크기가_5보다_큰_골_참여_사용자_목록, 유효한_골))
                .isInstanceOf(InvalidGoalException.InvalidInvalidUsersSize.class);
    }
    
    @Test
    void 골_팀_목록_수정시_참여자_목록_크기가_5보다_큰_경우_예외를_발생한다() {
        // given
        final Teams teams = new Teams(골_참여_사용자_목록, 유효한_골);
        
        // when & then
        assertThatThrownBy(() -> teams.updateTeams(크기가_5보다_큰_골_참여_사용자_목록, 유효한_골))
                .isInstanceOf(InvalidGoalException.InvalidInvalidUsersSize.class);
    }
}
