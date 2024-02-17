package com.backend.blooming.goal.application;

import com.backend.blooming.goal.application.dto.PokeDto;
import com.backend.blooming.goal.application.exception.ForbiddenGoalToPokeException;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.notification.application.NotificationService;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PokeService {

    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final NotificationService notificationService;

    public void poke(final PokeDto pokeDto) {
        final User sender = getUser(pokeDto.senderId());
        final User receiver = getUser(pokeDto.receiverId());
        final Goal goal = getGaol(pokeDto.goalId());

        validateGoalTeams(goal, sender, receiver);

        notificationService.sendPokeNotification(goal, sender, receiver);
    }

    private User getUser(final Long userId) {
        return userRepository.findByIdAndDeletedIsFalse(userId)
                             .orElseThrow(NotFoundUserException::new);
    }

    private Goal getGaol(final Long goalId) {
        return goalRepository.findByIdWithUserAndDeletedIsFalse(goalId)
                             .orElseThrow(NotFoundGoalException::new);
    }

    private void validateGoalTeams(final Goal goal, final User sender, final User receiver) {
        if (!goal.isTeam(sender)) {
            throw new ForbiddenGoalToPokeException.SenderNotInGoalTeam();
        }
        if (!goal.isTeam(receiver)) {
            throw new ForbiddenGoalToPokeException.ReceiverNotInGoalTeam();
        }
    }
}
