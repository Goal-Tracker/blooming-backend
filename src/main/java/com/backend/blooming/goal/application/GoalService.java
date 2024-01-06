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

    public Long createGoal(final CreateGoalDto createGoalDto) {
        final Goal goal = persistGoal(createGoalDto);
        final List<GoalTeam> goalTeams = createGoalTeams(createGoalDto.teamUserIds(), goal.getId());
        goal.updateGoalTeams(goalTeams);

        return goal.getId();
    }

    private Goal persistGoal(final CreateGoalDto createGoalDto) {
        validateGoalDatePeriod(createGoalDto.startDate(), createGoalDto.endDate());
        final User user = getValidUser(createGoalDto.managerId());

        final Goal goal = Goal.builder()
                              .name(createGoalDto.name())
                              .memo(createGoalDto.memo())
                              .startDate(createGoalDto.startDate())
                              .endDate(createGoalDto.endDate())
                              .managerId(user.getId())
                              .build();

        return goalRepository.save(goal);
    }

    private List<GoalTeam> createGoalTeams(final List<Long> goalTeamUserIds, final Long goalId) {
        final Goal goal = goalRepository.findByIdAndDeletedIsFalse(goalId)
                                        .orElseThrow(NotFoundGoalException::new);

        final List<GoalTeam> goalTeams = new ArrayList<>();

        for (final Long goalTeamUser : goalTeamUserIds) {
            final User user = getValidUser(goalTeamUser);
            final GoalTeam goalTeam = new GoalTeam(user, goal);
            final GoalTeam persistGoalTeam = goalTeamRepository.save(goalTeam);

            goalTeams.add(persistGoalTeam);
        }

        return goalTeams;
    }

    private void validateGoalDatePeriod(final LocalDate startDate, final LocalDate endDate) {
        final LocalDate nowDate = LocalDate.now();

        if (startDate.isBefore(nowDate)) {
            throw new InvalidGoalException.InvalidInvalidGoalStartDay();
        }
        if (endDate.isBefore(nowDate)) {
            throw new InvalidGoalException.InvalidInvalidGoalEndDay();
        }
        if (endDate.isBefore(startDate)) {
            throw new InvalidGoalException.InvalidInvalidGoalPeriod();
        }
    }

    private User getValidUser(final Long userId) {
        return userRepository.findByIdAndDeletedIsFalse(userId)
                             .orElseThrow(NotFoundUserException::new);
    }

    @Transactional(readOnly = true)
    public GoalDto readGoalById(final Long goalId) {
        final Goal goal = goalRepository.findByIdAndDeletedIsFalse(goalId)
                                        .orElseThrow(NotFoundGoalException::new);

        return GoalDto.from(goal);
    }
}
