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
import org.hibernate.annotations.CreationTimestamp;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString
public class Goal extends DateFormat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    ) {
        this.goalName = goalName;
        this.goalMemo = goalMemo;
        this.goalStartDay = goalStartDay;
        this.goalEndDay = goalEndDay;
        this.goalDays = goalDays;
        for (GoalTeam goalTeam:goalTeams){
            this.addGoalTeam(goalTeam);
        }
    }

    public boolean isGoalAvailable() throws ParseException {
        final Date goalStartDate = dateFormatter(goalStartDay);
        final Date goalEndDate = dateFormatter(goalEndDay);
        final Date nowDate = dateFormatter(LocalDate.now().toString());

        if (nowDate.compareTo(goalStartDate)<=0 && nowDate.compareTo(goalEndDate)>=0) {
            return true;
        }

        return false;
    }

    // 골 제목과 메모에 글자 수 제한을 둘 것인지 논의 필요
    // 골 시작일이 오늘 날짜보다 이전일 경우 생성불가
    // 골 마감일이 오늘 날짜보다 이후일 경우 생성불가
    // 골 시작일보다 골 마감일이 더 빠를 경우 생성불가
    // 골 일수가 1보다 작을 경우 생성불가

}
