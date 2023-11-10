package com.backend.blooming.goal.application;

import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.util.DateFormat;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class GoalService extends DateFormat{

    private final GoalRepository goalRepository;

    public Goal createGoal(CreateGoalDto createGoalDto){
        final Goal goal = Goal.builder()
                .goalName(createGoalDto.goalName())
                .goalMemo(createGoalDto.goalMemo())
                .goalStartDay(createGoalDto.goalStartDay())
                .goalEndDay(createGoalDto.goalEndDay())
                .goalDays(createGoalDto.goalDays())
                .build();

        final Goal persistGoal = goalRepository.save(goal);

        return persistGoal;
    }
}
