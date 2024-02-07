package com.backend.blooming.goal.domain;

import com.backend.blooming.goal.application.exception.DeleteGoalForbiddenException;
import com.backend.blooming.goal.application.exception.InvalidGoalException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class GoalTest extends GoalTestFixture {

    @Test
    void 골_메모를_빈_값_또는_null로_받은_경우_비어있는_값으로_저장한다() {
        // when
        final Goal goal = Goal.builder()
                              .name(골_제목)
                              .memo("")
                              .startDate(골_시작일)
                              .endDate(골_종료일)
                              .managerId(골_관리자_아이디)
                              .users(골_참여자_목록)
                              .build();

        // then
        assertThat(goal.getMemo()).isEmpty();
    }

    @Test
    void 골_생성시_골_팀을_생성한다() {
        // when
        final Goal goal = Goal.builder()
                              .name(골_제목)
                              .memo("골 메모")
                              .startDate(골_시작일)
                              .endDate(골_종료일)
                              .managerId(골_관리자_아이디)
                              .users(골_참여자_목록)
                              .build();

        // then
        assertThat(goal.getTeams().getTeams()).hasSize(2);
    }

    @Test
    void 골_참여자_목록이_5명_초과인_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> Goal.builder()
                                     .name(골_제목)
                                     .memo("골 메모")
                                     .startDate(골_시작일)
                                     .endDate(골_종료일)
                                     .managerId(골_관리자_아이디)
                                     .users(유효하지_않은_골_참여자_목록)
                                     .build())
                .isInstanceOf(InvalidGoalException.InvalidInvalidUsersSize.class);
    }

    @Test
    void 골_제목을_수정한다() {
        // given
        final Goal goal = 유효한_골;

        // when
        goal.updateName("골 제목 테스트");

        // then
        assertThat(goal.getName()).isEqualTo("골 제목 테스트");
    }

    @Test
    void 요청한_골_제목_글자수가_50자_이상이거나_빈_값인_경우_예외를_발생한다() {
        // given
        final Goal goal = 유효한_골;

        // when & then
        assertThatThrownBy(() -> goal.updateName("testtesttesttesttesttesttesttesttesttesttesttesttest"))
                .isInstanceOf(InvalidGoalException.InvalidInvalidGoalName.class);
        assertThatThrownBy(() -> goal.updateName(""))
                .isInstanceOf(InvalidGoalException.InvalidInvalidGoalName.class);
    }

    @Test
    void 골_메모를_수정한다() {
        // given
        final Goal goal = 유효한_골;

        // when
        goal.updateMemo("골 메모 테스트");

        // then
        assertThat(goal.getMemo()).isEqualTo("골 메모 테스트");
    }

    @Test
    void 요청한_골_메모가_빈_값인_경우_비어있는_값으로_저장한다() {
        // given
        final Goal goal = 유효한_골;

        // when
        goal.updateMemo("");

        // then
        assertThat(goal.getMemo()).isEmpty();
    }

    @Test
    void 골_종료날짜를_수정한다() {
        // given
        final Goal goal = 유효한_골;

        // when
        goal.updateEndDate(LocalDate.now().plusDays(20));

        // then
        assertThat(goal.getGoalTerm().getEndDate()).isEqualTo(LocalDate.now().plusDays(20));
    }

    @Test
    void 골_종료날짜가_현재_종료날짜보다_이전일_경우_예외를_발생한다() {
        // given
        final Goal goal = 유효한_골;

        // when & then
        assertThatThrownBy(() -> goal.updateEndDate(수정_요청한_골_종료일))
                .isInstanceOf(InvalidGoalException.InvalidInvalidUpdateEndDate.class);
    }

    @Test
    void 골_팀_목록을_수정한다() {
        // given
        final Goal goal = 유효한_골;

        // when
        goal.getTeams().updateTeams(수정_요청한_골_참여자_목록, goal);

        // then
        assertSoftly(softAssertions -> {
            final List<GoalTeam> teams = goal.getTeams().getTeams();
            assertThat(teams).hasSize(3);
            assertThat(teams.get(0).getUser().getId()).isEqualTo(기존_골_참여자.getId());
            assertThat(teams.get(0).getUser().getName()).isEqualTo(기존_골_참여자.getName());
            assertThat(teams.get(1).getUser().getId()).isEqualTo(기존_골_참여자2.getId());
            assertThat(teams.get(1).getUser().getName()).isEqualTo(기존_골_참여자2.getName());
            assertThat(teams.get(2).getUser().getId()).isEqualTo(추가된_골_참여_사용자.getId());
            assertThat(teams.get(2).getUser().getName()).isEqualTo(추가된_골_참여_사용자.getName());
        });
    }

    @Test
    void 골을_삭제한다() {
        // given
        final Goal goal = 유효한_골;

        // when
        goal.updateDeleted(골_관리자_아이디);

        // then
        assertThat(goal.isDeleted()).isTrue();
    }

    @Test
    void 골_삭제_요청한_사용자의_아이디가_골_관리자_아이디와_다른_경우_예외를_발생한다() {
        // given
        final Goal goal = 유효한_골;

        // when & then
        assertThatThrownBy(() -> goal.updateDeleted(골_관리자가_아닌_사용자_아이디))
                .isInstanceOf(DeleteGoalForbiddenException.class);
    }
}
