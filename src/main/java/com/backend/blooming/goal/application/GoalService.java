package com.backend.blooming.goal.application;

import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.GoalDto;
import com.backend.blooming.goal.application.exception.GoalException;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.goal.infrastructure.repository.GoalTeamRepository;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public GoalDto createGoal(CreateGoalDto createGoalDto) {
        final Goal goal = persistGoal(createGoalDto);
        final List<GoalTeam> goalTeams = createGoalTeams(createGoalDto.goalTeamUserIds(), goal.getId());
        goal.updateGoalTeams(goalTeams);

        return GoalDto.from(goal);
    }

    private Goal persistGoal(CreateGoalDto createGoalDto) {
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

    public List<GoalTeam> createGoalTeams(List<Long> goalTeamUserIds, Long goalId) {
        final Goal goal = goalRepository.findByIdAndDeletedIsFalse(goalId)
                                        .orElseThrow(GoalException.GoalNotFoundException::new);

        List<GoalTeam> goalTeams = new ArrayList<>();

        for (Long goalTeamUser : goalTeamUserIds) {
            // 유저 정보가 존재하는지 검증하는 exception은 pr #6 머지 후 리팩토링하면서 진행하겠습니다.
            final User user = userRepository.findById(goalTeamUser).orElseThrow(EntityNotFoundException::new);
            final GoalTeam goalTeam = new GoalTeam(user, goal);
            final GoalTeam persistGoalTeam = goalTeamRepository.save(goalTeam);

            goalTeams.add(persistGoalTeam);
        }

        return goalTeams;
    }

    public void validateGoalDatePeriod(LocalDate goalStartDay, LocalDate goalEndDay) {
        final LocalDate nowDate = LocalDate.now();

        if (goalStartDay.isBefore(nowDate)) {
            throw new GoalException.InvalidGoalStartDay();
        } else if (goalEndDay.isBefore(nowDate)) {
            throw new GoalException.InvalidGoalEndDay();
        } else if (goalEndDay.isBefore(goalStartDay)) {
            throw new GoalException.InvalidGoalPeriod();
        }
    }

    public void validateGoalDays(int goalDays) {
        if (goalDays < 1) {
            throw new GoalException.InvalidGoalDays();
        }
    }
}
