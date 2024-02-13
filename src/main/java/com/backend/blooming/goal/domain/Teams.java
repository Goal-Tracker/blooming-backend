package com.backend.blooming.goal.domain;

import com.backend.blooming.goal.application.exception.InvalidGoalException;
import com.backend.blooming.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class Teams {

    private static final int TEAMS_MAXIMUM_LENGTH = 5;

    @Column(nullable = false)
    @OneToMany(mappedBy = "goal", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<GoalTeam> goalTeams = new ArrayList<>();

    private Teams(final List<GoalTeam> goalTeams) {
        this.goalTeams.addAll(goalTeams);
    }

    public static Teams create(final List<User> users, final Goal goal) {
        validateUsersSize(users);
        final List<GoalTeam> goalTeams = users.stream()
                                              .map(user -> new GoalTeam(user, goal))
                                              .toList();
        return new Teams(goalTeams);
    }

    private static void validateUsersSize(final List<User> users) {
        if (users.isEmpty() || users.size() > TEAMS_MAXIMUM_LENGTH) {
            throw new InvalidGoalException.InvalidInvalidUsersSize();
        }
    }

    public void update(final List<User> users, final Goal goal) {
        validateUsersSize(users);
        final List<User> usersBeforeUpdate = this.goalTeams.stream()
                                                           .map(GoalTeam::getUser)
                                                           .toList();
        final List<GoalTeam> updatedUsers = users.stream()
                                                 .filter(user -> !usersBeforeUpdate.contains(user))
                                                 .map(user -> new GoalTeam(user, goal))
                                                 .toList();
        this.goalTeams.addAll(updatedUsers);
    }
}
