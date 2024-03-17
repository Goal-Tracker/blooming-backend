package com.backend.blooming.goal.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class GoalTeamTest extends GoalTeamTestFixture {

    @Test
    void 골_참여자가_골_초대를_수락하면_참을_반환한다() {
        // given
        final GoalTeam goalTeam = new GoalTeam(유효한_골_참여_사용자, 유효한_골);

        // when
        goalTeam.updateAccepted();

        // then
        assertThat(goalTeam.isAccepted()).isTrue();
    }

    @Test
    void 골_관리자는_항상_골_수락을_참으로_한다() {
        // when
        final GoalTeam goalTeam = new GoalTeam(유효한_사용자, 유효한_골);

        // then
        assertThat(goalTeam.isAccepted()).isTrue();
    }
}
