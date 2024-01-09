package com.backend.blooming.goal.domain;

import com.backend.blooming.goal.application.exception.InvalidGoalException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Embeddable
@Getter
public class GoalTerm {

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private long days;

    @Column(nullable = false)
    private long inProgressDays;

    public GoalTerm() {
    }

    public GoalTerm(final LocalDate startDate, final LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = getValidGoalDays(startDate, endDate);
        this.inProgressDays = getValidInProgressDay(startDate, endDate);
    }

    private long getValidGoalDays(final LocalDate startDate, final LocalDate endDate) {
        final long goalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        if (goalDays < 1 || goalDays > 100) {
            throw new InvalidGoalException.InvalidInvalidGoalDays();
        }
        return goalDays;
    }

    private long getValidInProgressDay(final LocalDate startDate, final LocalDate endDate) {
        final long goalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        final LocalDate nowDate = LocalDate.now();
        long inProgressDays = ChronoUnit.DAYS.between(startDate, nowDate) + 1;

        if (inProgressDays < 0) {
            throw new InvalidGoalException.InvalidInvalidGoalDays();
        }
        if (inProgressDays > goalDays) {
            inProgressDays = goalDays;
        }
        return inProgressDays;
    }
}
