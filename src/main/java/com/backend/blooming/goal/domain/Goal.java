package com.backend.blooming.goal.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString
public class Goal {

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

    @Column(nullable = false)
    private boolean goalAction;

    @CreationTimestamp
    @Column(nullable = false, length = 20, updatable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(length = 20)
    private LocalDateTime updatedAt;

    @Builder
    public Goal(
            final String goalName,
            final String goalMemo,
            final String goalStartDay,
            final String goalEndDay,
            final int goalDays,
            final boolean goalAction
    ) {
        this.goalName = goalName;
        this.goalMemo = goalMemo;
        this.goalStartDay = goalStartDay;
        this.goalEndDay = goalEndDay;
        this.goalDays = goalDays;
        this.goalAction = goalAction;
    }

}
