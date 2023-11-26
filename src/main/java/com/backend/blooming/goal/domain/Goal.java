package com.backend.blooming.goal.domain;

import com.backend.blooming.goal.application.util.DateFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "goal")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = "goalTeams")
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
    private Date goalStartDay;

    @Column(nullable = false)
    private Date goalEndDay;

    @Column(nullable = false)
    private int goalDays;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GoalTeam> goalTeams = new ArrayList<>();

    @Column(nullable = false)
    private boolean isDeleted;

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
            final Date goalStartDay,
            final Date goalEndDay,
            final int goalDays
    ){
        this.goalName = goalName;
        this.goalMemo = goalMemo;
        validStartDay(goalStartDay);
        validEndDay(goalEndDay);
        validGoal(goalStartDay, goalEndDay);
        this.goalStartDay = goalStartDay;
        this.goalEndDay = goalEndDay;
        validGoalDays(goalDays);
        this.goalDays = goalDays;
        this.goalTeams = new ArrayList<>();
    }

    public boolean isGoalAvailable() {
        final Date nowDate = dateFormatter(LocalDate.now().toString());

        if (nowDate.compareTo(goalStartDay)>0 && nowDate.compareTo(goalEndDay)>0) {
            log.info("이미 종료된 골입니다.");
            throw new IllegalArgumentException();
        }

        return true;
    }

    public void updateGoalTeams(List<GoalTeam> goalTeams){
        this.goalTeams = goalTeams;
    }

    public void validStartDay(Date goalStartDay) {
        final Date nowDate = dateFormatter(LocalDate.now().toString());
        if (goalStartDay.compareTo(nowDate)<0){
            throw new IllegalArgumentException("시작 날짜는 현재 날짜 이후여야합니다.");
        }
    }

    public void validEndDay(Date goalEndDay) {
        final Date nowDate = dateFormatter(LocalDate.now().toString());
        if (nowDate.compareTo(goalEndDay)>0){
            throw new IllegalArgumentException("종료 날짜는 현재 날짜 이후여야합니다.");
        }
    }

    public void validGoal(Date goalStartDay, Date goalEndDay){
        if (goalStartDay.compareTo(goalEndDay)>0){
            throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 이전이어야합니다.");
        }
    }

    public void validGoalDays(int goalDays){
        if (goalDays<1){
            throw new IllegalArgumentException("골 일자는 1 이상이어야합니다.");
        }
    }

    public List<Long> getGoalTeamIds(){
        List<Long> goalTeamIds = new ArrayList<>();
        for(GoalTeam goalTeam:goalTeams){
            goalTeamIds.add(goalTeam.getUser().getId());
        }
        return goalTeamIds;
    }

    public void updateIsDeleted(){
        if (!isDeleted){
            this.isDeleted = true;
        }
    }
}
