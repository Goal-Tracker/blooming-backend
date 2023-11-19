package com.backend.blooming.goal.application;

import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.util.DateFormat;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.goal.infrastructure.repository.GoalTeamRepository;
import com.backend.blooming.goal.presentation.dto.request.CreateGoalRequest;
import com.backend.blooming.goal.presentation.dto.response.CreateGoalResponse;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoalService extends DateFormat{

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final GoalTeamRepository goalTeamRepository;

    public Goal createGoal(CreateGoalDto createGoalDto) throws ParseException {
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
            final GoalTeam persistGoalTeam = goalTeamRepository.save(goalTeam);
            goalTeams.add(persistGoalTeam);
        }

        return goalTeams;
    }

    public CreateGoalDto createGoalDto(CreateGoalRequest createGoalRequest){
        List<Long> goalTeams = new ArrayList<>();

        for(String goalTeamUserName: createGoalRequest.goalTeamUserIds()){
            User user = userRepository.findByName(goalTeamUserName);
            goalTeams.add(user.getId());
        }

        final CreateGoalDto createGoalDto = new CreateGoalDto(
                createGoalRequest.goalName(),
                createGoalRequest.goalMemo(),
                createGoalRequest.goalStartDay(),
                createGoalRequest.goalEndDay(),
                createGoalRequest.goalDays(),
                goalTeams);

        return createGoalDto;
    }

    public CreateGoalResponse createGoalResponse(CreateGoalDto createGoalDto) throws ParseException {
        final Goal goal = createGoal(createGoalDto);
        final List<String> goalTeamUserIds = new ArrayList<>();

        for(GoalTeam goalTeam:goal.getGoalTeams()){
            goalTeamUserIds.add(goalTeam.getUser().getName());
        }

        return new CreateGoalResponse(
                goal.getId().toString(),
                goal.getGoalName(),
                goal.getGoalMemo(),
                goal.getGoalStartDay(),
                goal.getGoalEndDay(),
                goal.getGoalDays(),
                goalTeamUserIds);
    }
}
