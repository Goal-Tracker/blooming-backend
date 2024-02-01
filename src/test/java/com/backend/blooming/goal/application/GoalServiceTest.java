package com.backend.blooming.goal.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.goal.application.dto.ReadAllGoalDto;
import com.backend.blooming.goal.application.dto.ReadGoalDetailDto;
import com.backend.blooming.goal.application.exception.DeleteGoalForbiddenException;
import com.backend.blooming.goal.application.exception.InvalidGoalException;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.goal.application.exception.UpdateGoalForbiddenException;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class GoalServiceTest extends GoalServiceTestFixture {

    @Autowired
    private GoalService goalService;

    @Test
    void 새로운_골을_생성한다() {
        // when
        final Long goalId = goalService.createGoal(유효한_골_생성_dto);

        // then
        assertThat(goalId).isPositive();
    }

    @Test
    void 골_생성시_존재하지_않는_사용자가_관리자인_경우_예외를_발생한다() {
        // when
        assertThatThrownBy(() -> goalService.createGoal(존재하지_않는_사용자가_관리자인_골_생성_dto))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 골_생성시_친구가_아닌_사용자가_참여자로_있는_경우_예외를_발생한다() {
        // when
        assertThatThrownBy(() -> goalService.createGoal(친구가_아닌_사용자가_참여자로_있는_골_생성_dto))
                .isInstanceOf(InvalidGoalException.InvalidInvalidUserToParticipate.class);
    }

    @Test
    void 골_아이디로_해당_골_정보를_조회한다() {
        // when
        final ReadGoalDetailDto result = goalService.readGoalDetailById(유효한_골_아이디);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result.name()).isEqualTo(골_제목);
            softAssertions.assertThat(result.memo()).isEqualTo(골_메모);
            softAssertions.assertThat(result.startDate()).isEqualTo(골_시작일);
            softAssertions.assertThat(result.endDate()).isEqualTo(골_종료일);
            softAssertions.assertThat(result.days()).isEqualTo(골_날짜수);
            softAssertions.assertThat(result.managerId()).isEqualTo(유효한_사용자_아이디);
            softAssertions.assertThat(result.teams().get(0).id()).isEqualTo(유효한_골_dto.teams().get(0).id());
            softAssertions.assertThat(result.teams().get(0).name()).isEqualTo(유효한_골_dto.teams().get(0).name());
            softAssertions.assertThat(result.teams().get(0).color()).isEqualTo(유효한_골_dto.teams().get(0).color());
            softAssertions.assertThat(result.teams().get(0).statusMessage())
                          .isEqualTo(유효한_골_dto.teams().get(0).statusMessage());
            softAssertions.assertThat(result.teams().get(1).id()).isEqualTo(유효한_골_dto.teams().get(1).id());
        });
    }

    @Test
    void 존재하지_않는_골_아이디를_조회한_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> goalService.readGoalDetailById(존재하지_않는_골_아이디))
                .isInstanceOf(NotFoundGoalException.class);
    }

    @Test
    void 현재_로그인한_사용자가_참여한_골_중_현재_진행중인_모든_골_정보를_조회한다() {
        // when
        final ReadAllGoalDto result = goalService.readAllGoalByUserIdAndInProgress(
                유효한_사용자_아이디,
                LocalDate.now().plusDays(테스트를_위한_시스템_현재_시간_설정값)
        );

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result.goalInfos()).hasSize(2);
            softAssertions.assertThat(result.goalInfos().get(0).id()).isEqualTo(현재_진행중인_골1.getId());
            softAssertions.assertThat(result.goalInfos().get(0).name()).isEqualTo(현재_진행중인_골1.getName());
            softAssertions.assertThat(result.goalInfos().get(1).id()).isEqualTo(현재_진행중인_골2.getId());
            softAssertions.assertThat(result.goalInfos().get(1).name()).isEqualTo(현재_진행중인_골2.getName());
        });
    }

    @Test
    void 현재_로그인한_사용자가_참여한_골_중_종료된_모든_골_정보를_조회한다() {
        // when
        final ReadAllGoalDto result = goalService.readAllGoalByUserIdAndFinished(
                유효한_사용자_아이디,
                LocalDate.now().plusDays(테스트를_위한_시스템_현재_시간_설정값)
        );

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result.goalInfos()).hasSize(2);
            softAssertions.assertThat(result.goalInfos().get(0).id()).isEqualTo(이미_종료된_골1.getId());
            softAssertions.assertThat(result.goalInfos().get(0).name()).isEqualTo(이미_종료된_골1.getName());
            softAssertions.assertThat(result.goalInfos().get(1).id()).isEqualTo(이미_종료된_골2.getId());
            softAssertions.assertThat(result.goalInfos().get(1).name()).isEqualTo(이미_종료된_골2.getName());
        });
    }

    @Test
    void 요청받은_골의_아이디에_해당하는_골을_삭제한다() {
        // when & then
        assertThatCode(() -> goalService.delete(유효한_사용자_아이디, 유효한_골_아이디)).doesNotThrowAnyException();
    }

    @Test
    void 존재하지_않는_골_아이디를_삭제_요청했을_때_예외를_발생한다() {
        assertThatThrownBy(() -> goalService.delete(유효한_사용자_아이디, 유효하지_않은_골_아이디))
                .isInstanceOf(NotFoundGoalException.class);
    }

    @Test
    void 존재하지_않는_사용자가_삭제를_요청했을_때_예외를_발생한다() {
        assertThatThrownBy(() -> goalService.delete(존재하지_않는_사용자_아이디, 유효한_골_아이디))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 삭제_요청한_사용자가_관리자가_아닌_경우_예외를_발생한다() {
        assertThatThrownBy(() -> goalService.delete(골_관리자가_아닌_사용자_아이디, 유효한_골_아이디))
                .isInstanceOf(DeleteGoalForbiddenException.class);
    }

    @Test
    void 골을_요청받은_내용으로_수정한다() {
        // when
        final ReadGoalDetailDto result = goalService.update(유효한_사용자_아이디, 유효한_골_아이디, 수정_요청한_골_dto);

        // then
        assertSoftly(SoftAssertions -> {
            final List<ReadGoalDetailDto.GoalTeamDto> teams = result.teams();
            assertThat(result.name()).isEqualTo(수정한_제목);
            assertThat(result.memo()).isEqualTo(수정한_메모);
            assertThat(result.endDate()).isEqualTo(수정한_종료날짜);
            assertThat(teams).hasSize(3);
            assertThat(teams.get(0).name()).isEqualTo(현재_로그인한_사용자.getName());
            assertThat(teams.get(1).name()).isEqualTo(친구인_사용자.getName());
            assertThat(teams.get(2).name()).isEqualTo(친구인_사용자2.getName());
        });
    }

    @Test
    void 골_수정을_요청한_사용자가_존재하지_않을시_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> goalService.update(존재하지_않는_사용자_아이디, 유효한_골_아이디, 수정_요청한_골_dto))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 수정_요청한_골이_존재하지_않을시_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> goalService.update(유효한_사용자_아이디, 존재하지_않는_골_아이디, 수정_요청한_골_dto))
                .isInstanceOf(NotFoundGoalException.class);
    }

    @Test
    void 골_수정을_요청한_사용자가_권한이_없을시_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> goalService.update(골_관리자가_아닌_사용자_아이디, 유효한_골_아이디, 수정_요청한_골_dto))
                .isInstanceOf(UpdateGoalForbiddenException.ForbiddenUserToUpdate.class);
    }

    @Test
    void 수정_요청한_참여자_목록이_비어있거나_null인_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> goalService.update(유효한_사용자_아이디, 유효하지_않은_골_아이디, 골_참여자_목록이_비어있는_수정_요청_골_dto))
                .isInstanceOf(NotFoundGoalException.class);
    }
}
