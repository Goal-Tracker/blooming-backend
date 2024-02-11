package com.backend.blooming.goal.domain;

import com.backend.blooming.common.entity.BaseTimeEntity;
import com.backend.blooming.goal.application.exception.InvalidGoalException;
import com.backend.blooming.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = {"teams", "stamps"})
public class Goal extends BaseTimeEntity {

    private static final String MEMO_DEFAULT = "";
    private static final int TEAMS_MAXIMUM_LENGTH = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    private String memo;

    @Embedded
    private GoalTerm goalTerm;

    @Column(nullable = false)
    private Long managerId;

    @OneToMany(mappedBy = "goal", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<GoalTeam> teams = new ArrayList<>(TEAMS_MAXIMUM_LENGTH);

    @Column(nullable = false)
    private boolean deleted = false;

    @Builder
    private Goal(
            final String name,
            final String memo,
            final LocalDate startDate,
            final LocalDate endDate,
            final Long managerId,
            final List<User> users
    ) {
        this.name = name;
        this.memo = processDefaultMemo(memo);
        this.goalTerm = new GoalTerm(startDate, endDate);
        this.managerId = managerId;
        createGoalTeams(users);
    }

    private String processDefaultMemo(final String memo) {
        if (memo == null || memo.isEmpty()) {
            return MEMO_DEFAULT;
        }
        return memo;
    }

    private void createGoalTeams(final List<User> users) {
        validateUsersSize(users);
        users.forEach(user -> new GoalTeam(user, this));
    }

    private void validateUsersSize(final List<User> users) {
        if (users.size() > TEAMS_MAXIMUM_LENGTH) {
            throw new InvalidGoalException.InvalidInvalidUsersSize();
        }
    }
}
