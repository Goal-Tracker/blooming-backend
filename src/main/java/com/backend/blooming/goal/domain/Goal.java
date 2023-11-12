package com.backend.blooming.goal.domain;


import com.backend.blooming.goal.application.util.DateFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Builder;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "goal")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString
@Slf4j
public class Goal extends DateFormat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long id;

    @Column(nullable = false)
    private String goalName;

    @Column(columnDefinition = "text")
    private String goalMemo;

    @Column(nullable = false)
    private String goalStartDay;

    @Column(nullable = false)
    private String goalEndDay;

    @Column(nullable = false)
    private int goalDays;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GoalTeam> goalTeams = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, length = 20, updatable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(length = 20)
    private LocalDateTime updatedAt;

    public void addGoalTeam(GoalTeam goalTeam){
        goalTeams.add(goalTeam);
        goalTeam.updateGoal(this);
    }

    @Builder
    private Goal(
            final String goalName,
            final String goalMemo,
            final String goalStartDay,
            final String goalEndDay,
            final int goalDays,
            List<GoalTeam> goalTeams
    ) throws ParseException {
        this.goalName = goalName;
        this.goalMemo = goalMemo;
        validStartDay();
        validEndDay();
        validGoal();
        this.goalStartDay = goalStartDay;
        this.goalEndDay = goalEndDay;
        validGoalDays();
        this.goalDays = goalDays;
        for (GoalTeam goalTeam:goalTeams){
            this.addGoalTeam(goalTeam);
        }
    }

    public boolean isGoalAvailable() throws ParseException {
        final Date goalStartDate = dateFormatter(goalStartDay);
        final Date goalEndDate = dateFormatter(goalEndDay);
        final Date nowDate = dateFormatter(LocalDate.now().toString());

        if (nowDate.compareTo(goalStartDate)>0 && nowDate.compareTo(goalEndDate)>0) {
            log.info("이미 종료된 골입니다.");
            throw new IllegalArgumentException();
        }

        return true;
    }

    public void validStartDay() throws ParseException {
        final Date goalStartDate = dateFormatter(goalStartDay);
        final Date nowDate = dateFormatter(LocalDate.now().toString());
        if (goalStartDate.compareTo(nowDate)>0){
            throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 이전이어야합니다.");
        }
    }

    public void validEndDay() throws ParseException {
        final Date goalEndDate = dateFormatter(goalStartDay);
        final Date nowDate = dateFormatter(LocalDate.now().toString());
        if (nowDate.compareTo(goalEndDate)>0){
            throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 이전이어야합니다.");
        }
    }

    public void validGoal() throws ParseException {
        final Date goalStartDate = dateFormatter(goalStartDay);
        final Date goalEndDate = dateFormatter(goalStartDay);
        if (goalStartDate.compareTo(goalEndDate)>0){
            throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 이전이어야합니다.");
        }
    }

    public void validGoalDays(){
        if (goalDays<1){
            throw new IllegalArgumentException("골 일자는 1 이상이어야합니다.");
        }
    }

}
