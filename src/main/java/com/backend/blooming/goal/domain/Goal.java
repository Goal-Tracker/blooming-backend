package com.backend.blooming.goal.domain;

import jakarta.persistence.CascadeType;
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
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = "goalTeams")
@Slf4j
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
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

    @OneToMany(mappedBy = "goal", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GoalTeam> goalTeams = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted = false;

    @CreationTimestamp
    @Column(nullable = false, length = 20, updatable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(length = 20)
    private LocalDateTime updatedAt;

    @Builder
    private Goal(
            final String goalName,
            final String goalMemo,
            final LocalDate goalStartDay,
            final LocalDate goalEndDay,
            final int goalDays
    ) {
        this.goalName = goalName;
        this.goalMemo = goalMemo;
        this.goalStartDay = goalStartDay;
        this.goalEndDay = goalEndDay;
        this.goalDays = goalDays;
        this.goalTeams = new ArrayList<>();
    }

    public void updateGoalTeams(List<GoalTeam> goalTeams) {
        this.goalTeams = goalTeams;
    }

    public void updateDeleted() {
        this.deleted = true;
    }
}
