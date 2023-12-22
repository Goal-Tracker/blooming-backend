package com.backend.blooming.goal.application;

import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.GoalDto;
import com.backend.blooming.goal.application.exception.InvalidGoalException;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
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
        final List<GoalTeam> goalTeams = createGoalTeams(createGoalDto.teamUserIds(), goal.getId());
        goal.updateGoalTeams(goalTeams);

        return GoalDto.from(goal);
    }

    private Goal persistGoal(final CreateGoalDto createGoalDto) {
        final LocalDate startDate = LocalDate.parse(createGoalDto.startDate());
        final LocalDate endDate = LocalDate.parse(createGoalDto.endDate());

        validateGoalDatePeriod(startDate, endDate);
        validateGoalDays(createGoalDto.days());

        final Goal goal = Goal.builder()
                              .name(createGoalDto.name())
                              .memo(createGoalDto.memo())
                              .startDate(startDate)
                              .endDate(endDate)
                              .days(createGoalDto.days())
                              .managerId(createGoalDto.managerId())
                              .build();

        return goalRepository.save(goal);
    }

    private List<GoalTeam> createGoalTeams(final List<Long> goalTeamUserIds, final Long goalId) {
        final Goal goal = goalRepository.findByIdAndDeletedIsFalse(goalId)
                                        .orElseThrow(NotFoundGoalException::new);

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
            throw new InvalidGoalException.InvalidInvalidGoalStartDay();
        } else if (goalEndDay.isBefore(nowDate)) {
            throw new InvalidGoalException.InvalidInvalidGoalEndDay();
        } else if (goalEndDay.isBefore(goalStartDay)) {
            throw new InvalidGoalException.InvalidInvalidGoalPeriod();
        }
    }

    private void validateGoalDays(final int goalDays) {
        if (goalDays < 1) {
            throw new InvalidGoalException.InvalidInvalidGoalDays();
        }
    }

    @Transactional(readOnly = true)
    public GoalDto readGoalById(final Long goalId) {
        final Goal goal = goalRepository.findByIdAndDeletedIsFalse(goalId)
                                        .orElseThrow(NotFoundGoalException::new);

        return GoalDto.from(goal);
    }
}
