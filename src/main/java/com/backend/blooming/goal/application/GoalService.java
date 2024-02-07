package com.backend.blooming.goal.application;

import com.backend.blooming.friend.infrastructure.repository.FriendRepository;
import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.ReadAllGoalDto;
import com.backend.blooming.goal.application.dto.ReadGoalDetailDto;
import com.backend.blooming.goal.application.dto.UpdateGoalDto;
import com.backend.blooming.goal.application.exception.InvalidGoalException;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.goal.application.exception.UpdateGoalForbiddenException;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GoalService {

    private static final int TEAMS_MAXIMUM_LENGTH = 5;

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    public Long createGoal(final CreateGoalDto createGoalDto) {
        final List<User> users = getUsers(createGoalDto.teamUserIds());
        final Goal goal = persistGoal(createGoalDto, users);

        return goal.getId();
    }

    private List<User> getUsers(final List<Long> userIds) {
        return userRepository.findAllByUserIds(userIds);
    }

    private Goal persistGoal(final CreateGoalDto createGoalDto, final List<User> users) {
        final User user = getUser(createGoalDto.managerId());
        validateIsFriend(user.getId(), createGoalDto.teamUserIds());

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

    private void validateIsFriend(final Long userId, final List<Long> teamUserIds) {
        final Long countFriends = friendRepository.countByUserIdAndFriendIdsAndIsFriends(userId, teamUserIds);

        if (countFriends != ((long) teamUserIds.size() - 1)) {
            throw new InvalidGoalException.InvalidInvalidUserToParticipate();
        }
    }

    @Transactional(readOnly = true)
    public ReadGoalDetailDto readGoalDetailById(final Long goalId) {
        final Goal goal = getGoal(goalId);

        return ReadGoalDetailDto.from(goal);
    }

    private Goal getGoal(final Long id) {
        return goalRepository.findByIdAndDeletedIsFalse(id)
                             .orElseThrow(NotFoundGoalException::new);
    }

    @Transactional(readOnly = true)
    public ReadAllGoalDto readAllGoalByUserIdAndInProgress(final Long userId, final LocalDate now) {
        final List<Goal> goals = goalRepository.findAllByUserIdAndInProgress(userId, now);

        return ReadAllGoalDto.from(goals);
    }

    @Transactional(readOnly = true)
    public ReadAllGoalDto readAllGoalByUserIdAndFinished(final Long userId, final LocalDate now) {
        final List<Goal> goals = goalRepository.findAllByUserIdAndFinished(userId, now);

        return ReadAllGoalDto.from(goals);
    }

    @Transactional
    public ReadGoalDetailDto update(final Long userId, final Long goalId, final UpdateGoalDto updateGoalDto) {
        final User user = getUser(userId);
        final Goal goal = getGoal(goalId);
        validateUserToUpdate(goal.getManagerId(), user.getId());

        if (updateGoalDto.name() != null) {
            goal.updateName(updateGoalDto.name());
        }
        if (updateGoalDto.memo() != null) {
            goal.updateMemo(updateGoalDto.memo());
        }
        if (updateGoalDto.endDate() != null) {
            // TODO: 종료날짜의 경우 Json에서 null 값을 받아오면 LocalDate로 파싱할 수 없어 오류 발생하는 문제 해결해야함
            goal.updateEndDate(updateGoalDto.endDate());
        }
        if (updateGoalDto.teamUserIds() != null) {
            validateTeamsToUpdate(updateGoalDto.teamUserIds());
            final List<User> users = userRepository.findAllByUserIds(updateGoalDto.teamUserIds());
            goal.getTeams().updateTeams(users, goal);
        }

        return ReadGoalDetailDto.from(goal);
    }

    private void validateUserToUpdate(final Long managerId, final Long userId) {
        if (!managerId.equals(userId)) {
            throw new UpdateGoalForbiddenException.ForbiddenUserToUpdate();
        }
    }

    private void validateTeamsToUpdate(final List<Long> teamUserIds) {
        if (teamUserIds.isEmpty() || teamUserIds.size() > TEAMS_MAXIMUM_LENGTH) {
            throw new InvalidGoalException.InvalidInvalidUsersSize();
        }
    }

    @Transactional
    public void delete(final Long userId, final Long goalId) {
        final User user = getUser(userId);
        final Goal goal = getGoal(goalId);
        goal.updateDeleted(user.getId());
    }
}
