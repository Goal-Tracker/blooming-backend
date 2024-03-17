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
        final List<GoalTeam> actual = teams.update(수정할_골_참여_사용자_목록, 유효한_골);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).getUser().getId()).isEqualTo(추가된_골_참여자.getId());
            softAssertions.assertThat(actual.get(0).getUser().getName()).isEqualTo(추가된_골_참여자.getName());
            final List<GoalTeam> result = teams.getGoalTeams();
            softAssertions.assertThat(result).hasSize(3);
            softAssertions.assertThat(result.get(0).getUser().getId()).isEqualTo(골_참여자.getId());
            softAssertions.assertThat(result.get(0).getUser().getName()).isEqualTo(골_참여자.getName());
            softAssertions.assertThat(result.get(1).getUser().getId()).isEqualTo(골_참여자2.getId());
            softAssertions.assertThat(result.get(1).getUser().getName()).isEqualTo(골_참여자2.getName());
            softAssertions.assertThat(result.get(2).getUser().getId()).isEqualTo(추가된_골_참여자.getId());
            softAssertions.assertThat(result.get(2).getUser().getName()).isEqualTo(추가된_골_참여자.getName());
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

    @Test
    void 골_팀_목록에_포함된_사용자라면_참을_반환한다() {
        // given
        final Teams teams = Teams.create(골_참여_사용자_목록, 유효한_골);

        // when
        final boolean actual = teams.isTeam(골_참여자);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 골_팀_목록에_포함된_사용자가_아니라면_거짓을_반환한다() {
        // given
        final Teams teams = Teams.create(골_참여_사용자_목록, 유효한_골);

        // when
        final boolean actual = teams.isTeam(팀원이_아닌_사용자);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 골_팀_목록에_포함된_사용자의_골_초대를_수락한다() {
        // given
        final Teams teams = Teams.create(골_참여_사용자_목록, 유효한_골);

        // when
        teams.updateAccepted(골_참여자2.getId());
        final List<GoalTeam> acceptedGoalTeam = teams.getGoalTeams()
                                                     .stream()
                                                     .filter(GoalTeam::isAccepted)
                                                     .toList();
        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(acceptedGoalTeam).hasSize(2);
            softAssertions.assertThat(acceptedGoalTeam.get(0).getUser().getId()).isEqualTo(골_참여자.getId());
            softAssertions.assertThat(acceptedGoalTeam.get(1).getUser().getId()).isEqualTo(골_참여자2.getId());
        });
    }
}
