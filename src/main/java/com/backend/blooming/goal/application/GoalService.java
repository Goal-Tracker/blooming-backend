package com.backend.blooming.goal.application;

import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.util.DateFormat;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GoalService extends DateFormat{

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    public Goal createGoal(CreateGoalDto createGoalDto){
        List<GoalTeam> goalTeams = createGoalTeams(createGoalDto);

        final Goal goal = Goal.builder()
                .goalName(createGoalDto.goalName())
                .goalMemo(createGoalDto.goalMemo())
                .goalStartDay(createGoalDto.goalStartDay())
                .goalEndDay(createGoalDto.goalEndDay())
                .goalDays(createGoalDto.goalDays())
                .goalTeams(goalTeams)
                .build();

        final Goal persistGoal = goalRepository.save(goal);

        return persistGoal;
    }

    public List<GoalTeam> createGoalTeams(CreateGoalDto createGoalDto){
        List<GoalTeam> goalTeams = new ArrayList<>();

        for (Long goalTeamUser: createGoalDto.goalTeamUserIds()){
            final User user = userRepository.findById(goalTeamUser).orElseThrow(EntityNotFoundException::new);
            final GoalTeam goalTeam = GoalTeam.builder()
                    .user(user)
                    .build();
            goalTeams.add(goalTeam);
        }

        return goalTeams;
    }
}
