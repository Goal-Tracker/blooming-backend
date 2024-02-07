package com.backend.blooming.goal.domain;

import com.backend.blooming.common.entity.BaseTimeEntity;
import com.backend.blooming.goal.application.exception.DeleteGoalForbiddenException;
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
@ToString(exclude = "teams")
public class Goal extends BaseTimeEntity {

    private static final String MEMO_DEFAULT = "";
    private static final int TEAMS_MAXIMUM_LENGTH = 5;
    private static final int MAX_LENGTH_OF_NAME = 50;
    private static final int START_INDEX_OF_NAME = 0;
    private static final int END_INDEX_OF_NAME = 50;

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

    @Embedded
    private Teams teams;

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
        this.teams = new Teams(users, this);
    }

    private String processDefaultMemo(final String memo) {
        if (memo == null || memo.isEmpty()) {
            return MEMO_DEFAULT;
        }
        return memo;
    }

    public void updateName(final String name) {
        this.name = validateNameLength(name);
    }

    private String validateNameLength(final String name) {
        if (name.isBlank() || name.length() > MAX_LENGTH_OF_NAME) {
            throw new InvalidGoalException.InvalidInvalidGoalName();
        }

        return name;
    }

    public void updateMemo(final String memo) {
        this.memo = processDefaultMemo(memo);
    }

    public void updateEndDate(final LocalDate endDate) {
        this.goalTerm.updateEndDate(endDate);
    }

    public void updateDeleted(final Long userId) {
        validUserToDelete(userId);
        this.deleted = true;
    }

    private void validUserToDelete(final Long userId) {
        if (!this.getManagerId().equals(userId)) {
            throw new DeleteGoalForbiddenException();
        }
    }
}
