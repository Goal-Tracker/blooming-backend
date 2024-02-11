package com.backend.blooming.stamp.application;

import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.stamp.application.dto.CreateStampDto;
import com.backend.blooming.stamp.application.exception.CreateStampForbiddenException;
import com.backend.blooming.stamp.domain.Stamp;
import com.backend.blooming.stamp.domain.exception.InvalidStampException;
import com.backend.blooming.stamp.infrastructure.repository.StampRepository;
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
public class StampService {
    
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final StampRepository stampRepository;
    
    public Long createStamp(final CreateStampDto createStampDto) {
        final Goal goal = getGoal(createStampDto.goalId());
        final User user = getUser(createStampDto.userId());
        validateUserInGoalTeams(goal, user.getId());
        validateExistStamp(user.getId(), createStampDto.day());
        
        return persistStamp(createStampDto, goal, user).getId();
    }
    
    private Goal getGoal(final Long goalId) {
        return goalRepository.findByIdAndDeletedIsFalse(goalId)
                             .orElseThrow(NotFoundGoalException::new);
    }
    
    private User getUser(final Long userId) {
        return userRepository.findByIdAndDeletedIsFalse(userId)
                             .orElseThrow(NotFoundUserException::new);
    }
    
    private void validateUserInGoalTeams(final Goal goal, final Long userId) {
        final List<Long> teamUserIds = goal.getTeams()
                                           .stream()
                                           .map(goalTeam -> goalTeam.getUser().getId())
                                           .toList();
        if (!teamUserIds.contains(userId)) {
            throw new CreateStampForbiddenException();
        }
    }
    
    private void validateExistStamp(final Long userId, final int day) {
        final boolean isExistsStamp = stampRepository.existsByUserIdAndDayAndDeletedIsFalse(userId, day);
        if (isExistsStamp) {
            throw new InvalidStampException.InvalidStampToCreate();
        }
    }
    
    private Stamp persistStamp(final CreateStampDto createStampDto, final Goal goal, final User user) {
        final Stamp stamp = Stamp.builder()
                                 .goal(goal)
                                 .user(user)
                                 .day(createStampDto.day())
                                 .message(createStampDto.message())
                                 .build();
        
        return stampRepository.save(stamp);
    }
}
