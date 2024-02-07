package com.backend.blooming.goal.domain;

import com.backend.blooming.goal.application.exception.InvalidGoalException;
import com.backend.blooming.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class Teams {

    private static final int TEAMS_MAXIMUM_LENGTH = 5;

    @Column(nullable = false)
    @OneToMany(mappedBy = "Teams", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<GoalTeam> teams = new ArrayList<>();

    public Teams() {
    }

    public Teams(final List<User> users, final Goal goal) {
        validateUsersSize(users);
        users.forEach(user -> new GoalTeam(user, goal, this));
    }

    private void validateUsersSize(final List<User> users) {
        if (users.size() > TEAMS_MAXIMUM_LENGTH) {
            throw new InvalidGoalException.InvalidInvalidUsersSize();
        }
    }

    public void updateTeams(final List<User> users, final Goal goal) {
        validateUsersSize(users);
        final List<User> usersBeforeUpdate = this.teams.stream()
                                                       .map(GoalTeam::getUser)
                                                       .toList();
        users.forEach(user -> {
            if (!usersBeforeUpdate.contains(user)) {
                new GoalTeam(user, goal, goal.getTeams());
            }
        });
    }
}
