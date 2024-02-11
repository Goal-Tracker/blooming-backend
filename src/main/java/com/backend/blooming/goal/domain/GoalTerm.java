package com.backend.blooming.goal.domain;

import com.backend.blooming.goal.application.exception.InvalidGoalException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class GoalTerm {

    private static final int GOAL_DAYS_MINIMUM = 1;
    private static final int GOAL_DAYS_MAXIMUM = 100;
    private static final int COUNT_GOAL_DAYS = 1;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private long days;

    public GoalTerm() {
    }

    public GoalTerm(final LocalDate startDate, final LocalDate endDate) {
        validateGoalDatePeriod(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = getValidGoalDays(startDate, endDate);
    }

    private void validateGoalDatePeriod(final LocalDate startDate, final LocalDate endDate) {
        final LocalDate nowDate = LocalDate.now();

        if (startDate.isBefore(nowDate)) {
            throw new InvalidGoalException.InvalidInvalidGoalStartDay();
        }
        if (endDate.isBefore(nowDate)) {
            throw new InvalidGoalException.InvalidInvalidGoalEndDay();
        }
        if (endDate.isBefore(startDate)) {
            throw new InvalidGoalException.InvalidInvalidGoalPeriod();
        }
    }

    private long getValidGoalDays(final LocalDate startDate, final LocalDate endDate) {
        final long goalDays = ChronoUnit.DAYS.between(startDate, endDate) + COUNT_GOAL_DAYS;

        if (goalDays < GOAL_DAYS_MINIMUM || goalDays > GOAL_DAYS_MAXIMUM) {
            throw new InvalidGoalException.InvalidInvalidGoalDays();
        }

        return goalDays;
    }
}
