package com.backend.blooming.goal.domain;

import com.backend.blooming.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
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
@ToString(exclude = "goalTeams")
public class Goal extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String goalName;

    @Column(columnDefinition = "text")
    private String goalMemo;

    @Column(nullable = false)
    private LocalDate goalStartDay;

    @Column(nullable = false)
    private LocalDate goalEndDay;

    @Column(nullable = false)
    private int goalDays;

    @Column(nullable = false)
    private Long goalManagerId;

    @OneToMany(mappedBy = "goal", fetch = FetchType.LAZY)
    private List<GoalTeam> goalTeams = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted = false;

    @Builder
    private Goal(
            final String goalName,
            final String goalMemo,
            final LocalDate goalStartDay,
            final LocalDate goalEndDay,
            final int goalDays,
            final Long goalManagerId
    ) {
        this.goalName = goalName;
        this.goalMemo = goalMemo;
        this.goalStartDay = goalStartDay;
        this.goalEndDay = goalEndDay;
        this.goalDays = goalDays;
        this.goalManagerId = goalManagerId;
        this.goalTeams = new ArrayList<>();
    }

    public void updateGoalTeams(List<GoalTeam> goalTeams) {
        this.goalTeams = goalTeams;
    }
}
