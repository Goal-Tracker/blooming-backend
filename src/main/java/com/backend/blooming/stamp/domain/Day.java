package com.backend.blooming.stamp.domain;

import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTerm;
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

    public Day(final GoalTerm goalTerm, final int day) {
        this.day = validateDay(goalTerm, day);
    }

    private int validateDay(final GoalTerm goalTerm, final int day) {
        final long nowStampDay = ChronoUnit.DAYS.between(goalTerm.getStartDate(), LocalDate.now()) + 1;

        if (day > nowStampDay) {
            throw new InvalidStampException.InvalidStampDayFuture();
        }
        if (day > goalTerm.getDays()) {
            throw new InvalidStampException.InvalidStampDay();
        }
        if (goalTerm.getStartDate().isAfter(LocalDate.now())) {
            throw new InvalidStampException.InvalidStampDay();
        }

        return day;
    }
}
