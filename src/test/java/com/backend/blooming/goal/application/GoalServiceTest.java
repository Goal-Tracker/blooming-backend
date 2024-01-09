package com.backend.blooming.goal.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.goal.application.dto.ReadAllGoalDto;
import com.backend.blooming.goal.application.dto.ReadGoalDetailDto;
import com.backend.blooming.goal.application.exception.InvalidGoalException;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    void 골_생성시_존재하지_않는_사용자가_참여자로_있는_경우_예외를_발생한다() {
        // when
        assertThatThrownBy(() -> goalService.createGoal(존재하지_않는_사용자가_참여자로_있는_골_생성_dto))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 골_시작날짜가_현재보다_이전인_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> goalService.createGoal(골_시작날짜가_현재보다_이전인_골_생성_dto))
                .isInstanceOf(InvalidGoalException.InvalidInvalidGoalStartDay.class);
    }

    @Test
    void 골_종료날짜가_현재보다_이전인_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> goalService.createGoal(골_종료날짜가_현재보다_이전인_골_생성_dto))
                .isInstanceOf(InvalidGoalException.InvalidInvalidGoalEndDay.class);
    }

    @Test
    void 골_종료날짜가_시작날짜보다_이전인_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> goalService.createGoal(골_종료날짜가_시작날짜보다_이전인_골_생성_dto))
                .isInstanceOf(InvalidGoalException.InvalidInvalidGoalPeriod.class);
    }

    @Test
    void 골_날짜가_100_초과인_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> goalService.createGoal(골_날짜수가_100_초과인_골_생성_dto))
                .isInstanceOf(InvalidGoalException.InvalidInvalidGoalDays.class);
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
            softAssertions.assertThat(result.inProgressDays()).isEqualTo(현재_진행중인_날짜수);
            softAssertions.assertThat(result.managerId()).isEqualTo(유효한_사용자_아이디);
            softAssertions.assertThat(result.goalTeamsWithUserName())
                          .usingRecursiveComparison()
                          .isEqualTo(골1에_참여한_사용자_정보를_포함한_골_팀_리스트);
        });
    }

    @Test
    void 존재하지_않는_골_아이디를_조회한_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> goalService.readGoalDetailById(존재하지_않는_골_아이디))
                .isInstanceOf(NotFoundGoalException.class);
    }

    @Test
    void 현재_로그인한_사용자가_참여한_모든_골_정보를_조회한다() {
        // when
        final List<ReadAllGoalDto> result = goalService.readAllGoalByUserId(유효한_사용자_아이디);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).hasSize(3);
            softAssertions.assertThat(result.get(0).id()).isEqualTo(유효한_골.getId());
            softAssertions.assertThat(result.get(0).name()).isEqualTo(유효한_골.getName());
            softAssertions.assertThat(result.get(0).startDate()).isEqualTo(유효한_골.getGoalTerm().getStartDate());
            softAssertions.assertThat(result.get(0).endDate()).isEqualTo(유효한_골.getGoalTerm().getEndDate());
            softAssertions.assertThat(result.get(0).days()).isEqualTo(유효한_골.getGoalTerm().getDays());
            softAssertions.assertThat(result.get(0).inProgressDays()).isEqualTo(유효한_골.getGoalTerm().getInProgressDays());
            softAssertions.assertThat(result.get(0).goalTeamsWithUserName())
                          .usingRecursiveComparison()
                          .isEqualTo(골1에_참여한_사용자_정보를_포함한_골_팀_리스트);
            softAssertions.assertThat(result.get(1).id()).isEqualTo(유효한_골2.getId());
            softAssertions.assertThat(result.get(1).name()).isEqualTo(유효한_골2.getName());
            softAssertions.assertThat(result.get(1).startDate()).isEqualTo(유효한_골2.getGoalTerm().getStartDate());
            softAssertions.assertThat(result.get(1).endDate()).isEqualTo(유효한_골2.getGoalTerm().getEndDate());
            softAssertions.assertThat(result.get(1).days()).isEqualTo(유효한_골2.getGoalTerm().getDays());
            softAssertions.assertThat(result.get(1).inProgressDays()).isEqualTo(유효한_골2.getGoalTerm().getInProgressDays());
            softAssertions.assertThat(result.get(1).goalTeamsWithUserName())
                          .usingRecursiveComparison()
                          .isEqualTo(골2에_참여한_사용자_정보를_포함한_골_팀_리스트);
            softAssertions.assertThat(result.get(2).id()).isEqualTo(유효한_골3.getId());
            softAssertions.assertThat(result.get(2).name()).isEqualTo(유효한_골3.getName());
            softAssertions.assertThat(result.get(2).startDate()).isEqualTo(유효한_골3.getGoalTerm().getStartDate());
            softAssertions.assertThat(result.get(2).endDate()).isEqualTo(유효한_골3.getGoalTerm().getEndDate());
            softAssertions.assertThat(result.get(2).days()).isEqualTo(유효한_골3.getGoalTerm().getDays());
            softAssertions.assertThat(result.get(2).inProgressDays()).isEqualTo(유효한_골3.getGoalTerm().getInProgressDays());
            softAssertions.assertThat(result.get(2).goalTeamsWithUserName())
                          .usingRecursiveComparison()
                          .isEqualTo(골3에_참여한_사용자_정보를_포함한_골_팀_리스트);
        });
    }
}
