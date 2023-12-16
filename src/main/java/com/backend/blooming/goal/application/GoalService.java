package com.backend.blooming.goal.application;

import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.GoalDto;
import com.backend.blooming.goal.application.exception.GoalException;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.goal.infrastructure.repository.GoalTeamRepository;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final GoalTeamRepository goalTeamRepository;

    public GoalDto createGoal(final CreateGoalDto createGoalDto) {
        final Goal goal = persistGoal(createGoalDto);
        final List<GoalTeam> goalTeams = createGoalTeams(createGoalDto.goalTeamUserIds(), goal.getId());
        goal.updateGoalTeams(goalTeams);

        return GoalDto.from(goal);
    }

    private Goal persistGoal(final CreateGoalDto createGoalDto) {
        final LocalDate goalStartDay = LocalDate.parse(createGoalDto.goalStartDay());
        final LocalDate goalEndDay = LocalDate.parse(createGoalDto.goalEndDay());

        validateGoalDatePeriod(goalStartDay, goalEndDay);
        validateGoalDays(createGoalDto.goalDays());

        final Goal goal = Goal.builder()
                              .goalName(createGoalDto.goalName())
                              .goalMemo(createGoalDto.goalMemo())
                              .goalStartDay(goalStartDay)
                              .goalEndDay(goalEndDay)
                              .goalDays(createGoalDto.goalDays())
                              .build();

        return goalRepository.save(goal);
    }

    private List<GoalTeam> createGoalTeams(final List<Long> goalTeamUserIds, final Long goalId) {
        final Goal goal = goalRepository.findByIdAndDeletedIsFalse(goalId)
                                        .orElseThrow(GoalException.GoalNotFoundException::new);

        final List<GoalTeam> goalTeams = new ArrayList<>();

        for (final Long goalTeamUser : goalTeamUserIds) {
            final User user = userRepository.findByIdAndDeletedIsFalse(goalTeamUser)
                                            .orElseThrow(NotFoundUserException::new);
            final GoalTeam goalTeam = new GoalTeam(user, goal);
            final GoalTeam persistGoalTeam = goalTeamRepository.save(goalTeam);

            goalTeams.add(persistGoalTeam);
        }

        return goalTeams;
    }

    private void validateGoalDatePeriod(final LocalDate goalStartDay, final LocalDate goalEndDay) {
        final LocalDate nowDate = LocalDate.now();

        if (goalStartDay.isBefore(nowDate)) {
            throw new GoalException.InvalidGoalStartDay();
        } else if (goalEndDay.isBefore(nowDate)) {
            throw new GoalException.InvalidGoalEndDay();
        } else if (goalEndDay.isBefore(goalStartDay)) {
            throw new GoalException.InvalidGoalPeriod();
        }
    }

    private void validateGoalDays(final int goalDays) {
        if (goalDays < 1) {
            throw new GoalException.InvalidGoalDays();
        }
    }

    @Transactional(readOnly = true)
    public GoalDto readGoalById(final Long goalId) {
        final Goal goal = goalRepository.findByIdAndDeletedIsFalse(goalId)
                                        .orElseThrow(GoalException.GoalNotFoundException::new);

        return GoalDto.from(goal);
    }
}
