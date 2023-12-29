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

    public GoalTerm() {
    }

    public GoalTerm(final LocalDate startDate, final LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        validateGoalDays(startDate, endDate);
        this.days = ChronoUnit.DAYS.between(startDate, endDate);
    }

    private void validateGoalDays(final LocalDate startDate, final LocalDate endDate) {
        final long goalDays = ChronoUnit.DAYS.between(startDate, endDate);

        if (goalDays < 1 || goalDays > 100) {
            throw new InvalidGoalException.InvalidInvalidGoalDays();
        }
    }
}
