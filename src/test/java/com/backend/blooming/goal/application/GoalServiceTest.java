package com.backend.blooming.goal.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.goal.application.dto.ReadGoalDetailDto;
import com.backend.blooming.goal.application.exception.InvalidGoalException;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
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
            softAssertions.assertThat(result.GoalTeamWithUserInfo().get(0).id()).isEqualTo(유효한_골_dto.GoalTeamWithUserInfo().get(0).id());
            softAssertions.assertThat(result.GoalTeamWithUserInfo().get(0).name()).isEqualTo(유효한_골_dto.GoalTeamWithUserInfo().get(0).name());
            softAssertions.assertThat(result.GoalTeamWithUserInfo().get(0).color()).isEqualTo(유효한_골_dto.GoalTeamWithUserInfo().get(0).color());
            softAssertions.assertThat(result.GoalTeamWithUserInfo().get(0).statusMessage()).isEqualTo(유효한_골_dto.GoalTeamWithUserInfo().get(0).statusMessage());
            softAssertions.assertThat(result.GoalTeamWithUserInfo().get(1).id()).isEqualTo(유효한_골_dto.GoalTeamWithUserInfo().get(1).id());
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
        final ReadAllGoalDto result = goalService.readAllGoalByUserIdAndInProgress(유효한_사용자_아이디, LocalDate.now().plusDays(테스트를_위한_시스템_현재_시간_설정값));

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result.goalInfoDtos()).hasSize(2);
            softAssertions.assertThat(result.goalInfoDtos().get(0).id()).isEqualTo(유효한_골.getId());
            softAssertions.assertThat(result.goalInfoDtos().get(0).name()).isEqualTo(유효한_골.getName());
            softAssertions.assertThat(result.goalInfoDtos().get(1).id()).isEqualTo(현재_진행중인_골.getId());
            softAssertions.assertThat(result.goalInfoDtos().get(1).name()).isEqualTo(현재_진행중인_골.getName());
        });
    }

    @Test
    void 현재_로그인한_사용자가_참여한_골_중_종료된_모든_골_정보를_조회한다() {
        // when
        final ReadAllGoalDto result = goalService.readAllGoalByUserIdAndFinished(유효한_사용자_아이디, LocalDate.now().plusDays(테스트를_위한_시스템_현재_시간_설정값));

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result.goalInfoDtos()).hasSize(2);
            softAssertions.assertThat(result.goalInfoDtos().get(0).id()).isEqualTo(이미_종료된_골1.getId());
            softAssertions.assertThat(result.goalInfoDtos().get(0).name()).isEqualTo(이미_종료된_골1.getName());
            softAssertions.assertThat(result.goalInfoDtos().get(1).id()).isEqualTo(이미_종료된_골2.getId());
            softAssertions.assertThat(result.goalInfoDtos().get(1).name()).isEqualTo(이미_종료된_골2.getName());
        });
    }
}
