package com.backend.blooming.goal.application;

import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.ReadAllGoalDto;
import com.backend.blooming.goal.application.dto.ReadGoalDetailDto;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    public Long createGoal(final CreateGoalDto createGoalDto) {
        final Goal goal = persistGoal(createGoalDto);

        return goal.getId();
    }

    private Goal persistGoal(final CreateGoalDto createGoalDto) {
        final User user = getUser(createGoalDto.managerId());
        final List<User> users = createGoalDto.teamUserIds()
                                              .stream()
                                              .map(this::getUser)
                                              .toList();

        final Goal goal = Goal.builder()
                              .name(createGoalDto.name())
                              .memo(createGoalDto.memo())
                              .startDate(createGoalDto.startDate())
                              .endDate(createGoalDto.endDate())
                              .managerId(user.getId())
                              .users(users)
                              .build();

        return goalRepository.save(goal);
    }

    private User getUser(final Long userId) {
        return userRepository.findByIdAndDeletedIsFalse(userId)
                             .orElseThrow(NotFoundUserException::new);
    }

    @Transactional(readOnly = true)
    public ReadGoalDetailDto readGoalDetailById(final Long goalId) {
        final Goal goal = goalRepository.findByIdAndDeletedIsFalse(goalId)
                                        .orElseThrow(NotFoundGoalException::new);

        return ReadGoalDetailDto.from(goal);
    }

    @Transactional(readOnly = true)
    public ReadAllGoalDto readAllGoalByUserId(final Long userId) {
        final List<Goal> goals = goalRepository.findAllByUserIdAndDeletedIsFalse(userId);

        return ReadAllGoalDto.from(goals);
    }
}
