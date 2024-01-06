package com.backend.blooming.goal.domain;

import com.backend.blooming.common.entity.BaseTimeEntity;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(columnDefinition = "text")
    private String memo;

    @Embedded
    private GoalTerm goalTerm;

    @Column(nullable = false)
    private Long managerId;

    @OneToMany(mappedBy = "goal", fetch = FetchType.LAZY)
    private List<GoalTeam> teams = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted = false;

    @Builder
    private Goal(
            final String name,
            final String memo,
            final LocalDate startDate,
            final LocalDate endDate,
            final Long managerId
    ) {
        this.name = name;
        this.memo = memo;
        this.goalTerm = new GoalTerm(startDate, endDate);
        this.managerId = managerId;
        this.teams = new ArrayList<>();
    }

    public void updateGoalTeams(List<GoalTeam> teams) {
        this.teams = teams;
    }
}
