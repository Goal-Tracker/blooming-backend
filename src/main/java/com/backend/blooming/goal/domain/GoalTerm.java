package com.backend.blooming.goal.domain;

import com.backend.blooming.goal.application.exception.InvalidGoalException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.FutureOrPresent;
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

    @Column(nullable = false)
    @FutureOrPresent
    private LocalDate startDate;

    @Column(nullable = false)
    @FutureOrPresent
    private LocalDate endDate;

    @Column(nullable = false)
    private long days;

    @Column(nullable = false)
    private long inProgressDays;

    public GoalTerm() {
    }

    public GoalTerm(final LocalDate startDate, final LocalDate endDate) {
        validateGoalDatePeriod(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = getValidGoalDays(startDate, endDate);
        this.inProgressDays = getValidInProgressDay(startDate, endDate);
    }

    private void validateGoalDatePeriod(final LocalDate startDate,
                                        final LocalDate endDate) {
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

    private long getValidGoalDays(final LocalDate startDate,
                                  final LocalDate endDate) {
        final long goalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        if (goalDays < 1 || goalDays > 100) {
            throw new InvalidGoalException.InvalidInvalidGoalDays();
        }
        return goalDays;
    }

    private long getValidInProgressDay(final LocalDate startDate,
                                       final LocalDate endDate) {
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
