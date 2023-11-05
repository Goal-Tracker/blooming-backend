package com.backend.blooming.goal.application;

import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.util.DateFormat;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class GoalService extends DateFormat{

    private final GoalRepository goalRepository;

    public Goal createGoal(CreateGoalDto createGoalDto) throws ParseException {

        boolean goalAction = isGoalAvailable(createGoalDto.goalStartDay(), createGoalDto.goalEndDay());

        final Goal goal = new Goal(
                createGoalDto.goalName(),
                createGoalDto.goalMemo(),
                createGoalDto.goalStartDay(),
                createGoalDto.goalEndDay(),
                createGoalDto.goalDays(),
                goalAction
        );

        final Goal persistGoal = goalRepository.save(goal);

        return persistGoal;
    }

    public boolean isGoalAvailable(String goalStartDay, String goalEndDay) throws ParseException {
        final Date goalStartDate = dateFormatter(goalStartDay);
        final Date goalEndDate = dateFormatter(goalEndDay);
        final Date nowDate = dateFormatter(LocalDate.now().toString());

        if (nowDate.compareTo(goalStartDate)>=0 && nowDate.compareTo(goalEndDate)<=0) {
            return true;
        }
        return false;
    }
}
