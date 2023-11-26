package com.backend.blooming.goal.application;

import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.GoalDto;
import com.backend.blooming.goal.application.exception.GoalException;
import com.backend.blooming.goal.application.util.DateFormat;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.goal.infrastructure.repository.GoalTeamRepository;
import com.backend.blooming.goal.presentation.dto.request.GoalRequest;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoalService extends DateFormat {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final GoalTeamRepository goalTeamRepository;

    public GoalDto createGoal(CreateGoalDto createGoalDto) {
        final Goal goal = persistGoal(createGoalDto);
        final List<GoalTeam> goalTeams = createGoalTeams(createGoalDto.goalTeamUserIds(), goal);
        goal.updateGoalTeams(goalTeams);

        return GoalDto.from(goal);
    }

    public Goal persistGoal(CreateGoalDto createGoalDto) {
        final Date goalStartDay = dateFormatter(createGoalDto.goalStartDay());
        final Date goalEndDay = dateFormatter(createGoalDto.goalEndDay());

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

    public List<GoalTeam> createGoalTeams(List<Long> goalTeamUserIds, Goal goal) {
        List<GoalTeam> goalTeams = new ArrayList<>();

        for (Long goalTeamUser : goalTeamUserIds) {
            final User user = userRepository.findById(goalTeamUser).orElseThrow(EntityNotFoundException::new);

            final GoalTeam goalTeam = GoalTeam.builder()
                    .user(user)
                    .goal(goal)
                    .build();
            final GoalTeam persistGoalTeam = goalTeamRepository.save(goalTeam);
            goalTeams.add(persistGoalTeam);
        }

        return goalTeams;
    }

    public void validateGoalDatePeriod(Date goalStartDay, Date goalEndDay) {
        final Date localDate = dateFormatter(String.valueOf(LocalDate.now()));

        if (goalStartDay.compareTo(localDate) < 0) {
            throw new GoalException.InvalidGoalStartDay();
        } else if (goalEndDay.compareTo(localDate) < 0) {
            throw new GoalException.InvalidGoalEndDay();
        } else if (goalEndDay.compareTo(goalStartDay) < 0) {
            throw new GoalException.InvalidGoalPeriod();
        }
    }

    public void validateGoalDays(int goalDays) {
        if (goalDays < 1) {
            throw new GoalException.InvalidGoalDays();
        }
    }

    public void deleteGoal(GoalRequest request) {
        List<GoalTeam> goalTeams;
//        final Goal goal = goalRepository.findById(request.id())
//                .orElseThrow(() -> new NotFoundGoalException("골을 조회할 수 없습니다."));
//        if (goal.isDeleted() != true) {
//            goal.updateIsDeleted();
//        }
//        if (goal.getGoalTeams().size() != 0) {
//            goalTeams = goal.getGoalTeams();
//            for (GoalTeam goalTeam : goalTeams) {
//                if (goalTeam.isDeleted() != true) {
//                    goalTeam.updateIsDeleted();
//                }
//            }
//        }
    }
}
