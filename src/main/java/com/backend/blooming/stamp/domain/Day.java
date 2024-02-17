package com.backend.blooming.stamp.domain;

import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.stamp.domain.exception.InvalidStampException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class Day {

    @Column(name = "stamp_day", nullable = false)
    private int day;

    public Day(final Goal goal, final int day) {
        this.day = validateDay(goal, day);
    }

    private int validateDay(final Goal goal, final int day) {
        final long nowStampDay = ChronoUnit.DAYS.between(goal.getGoalTerm().getStartDate(), LocalDate.now()) + 1;

        if (day > nowStampDay) {
            throw new InvalidStampException.InvalidStampDayFuture();
        }
        if (day > goal.getGoalTerm().getDays()) {
            throw new InvalidStampException.InvalidStampDay();
        }
        if (goal.getGoalTerm().getStartDate().isAfter(LocalDate.now())) {
            throw new InvalidStampException.InvalidStampDay();
        }

        return day;
    }
}
