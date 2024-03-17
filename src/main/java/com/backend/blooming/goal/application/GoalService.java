package com.backend.blooming.goal.application;

import com.backend.blooming.friend.infrastructure.repository.FriendRepository;
import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.ReadAllGoalDto;
import com.backend.blooming.goal.application.dto.ReadGoalDetailDto;
import com.backend.blooming.goal.application.dto.UpdateGoalDto;
import com.backend.blooming.goal.application.exception.InvalidGoalException;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.goal.application.exception.ReadGoalForbiddenException;
import com.backend.blooming.goal.application.exception.UpdateGoalForbiddenException;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.stamp.domain.Stamp;
import com.backend.blooming.stamp.infrastructure.repository.StampRepository;
import com.backend.blooming.notification.application.NotificationService;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import com.backend.blooming.common.util.DayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final StampRepository stampRepository;
    private final NotificationService notificationService;

    public Long createGoal(final CreateGoalDto createGoalDto) {
        final List<User> users = getUsers(createGoalDto.teamUserIds());
        final Goal goal = persistGoal(createGoalDto, users);

        notificationService.sendRequestGoalNotification(goal, goal.getTeams());

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
    public ReadGoalDetailDto readGoalDetailById(final Long goalId, final Long userId) {
        final Goal goal = getGoal(goalId);
        final User user = getUser(userId);
        validateUserInGoalTeams(goal, user);

        final List<Long> usersUploadedStamp = getUsersUploadedStamp(goal);

        return ReadGoalDetailDto.of(goal, usersUploadedStamp);
    }

    private Goal getGoal(final Long id) {
        return goalRepository.findByIdAndDeletedIsFalse(id)
                             .orElseThrow(NotFoundGoalException::new);
    }

    private void validateUserInGoalTeams(final Goal goal, final User user) {
        if (!goal.isTeam(user)) {
            throw new ReadGoalForbiddenException();
        }
    }

    private List<Long> getUsersUploadedStamp(final Goal goal) {
        final int nowGoalDay = DayUtil.getNowDay(goal.getGoalTerm().getStartDate());
        final List<Stamp> todayStamps = stampRepository.findAllByDayAndDeletedIsFalse(nowGoalDay);

        return todayStamps.stream()
                          .map(stamp -> stamp.getUser().getId())
                          .toList();
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

    public ReadGoalDetailDto update(final Long userId, final Long goalId, final UpdateGoalDto updateGoalDto) {
        final User user = getUser(userId);
        final Goal goal = getGoal(goalId);
        validateUserToUpdate(goal, user.getId());
        updateGoal(updateGoalDto, goal);
        final List<Long> usersUploadedStamp = getUsersUploadedStamp(goal);

        return ReadGoalDetailDto.of(goal, usersUploadedStamp);
    }

    private void validateUserToUpdate(final Goal goal, final Long userId) {
        if (!goal.isManager(userId)) {
            throw new UpdateGoalForbiddenException.ForbiddenUserToUpdate();
        }
    }

    private void updateGoal(final UpdateGoalDto updateGoalDto, final Goal goal) {
        if (updateGoalDto.name() != null) {
            goal.updateName(updateGoalDto.name());
        }
        if (updateGoalDto.memo() != null) {
            goal.updateMemo(updateGoalDto.memo());
        }
        if (updateGoalDto.endDate() != null) {
            goal.updateEndDate(updateGoalDto.endDate());
        }
        if (updateGoalDto.teamUserIds() != null) {
            final List<User> users = userRepository.findAllByUserIds(updateGoalDto.teamUserIds());
            final List<GoalTeam> updateTeams = goal.updateTeams(users);
            notificationService.sendRequestGoalNotification(goal, updateTeams);
        }
    }

    public void delete(final Long userId, final Long goalId) {
        final User user = getUser(userId);
        final Goal goal = getGoal(goalId);
        goal.updateDeleted(user.getId());
    }
}
