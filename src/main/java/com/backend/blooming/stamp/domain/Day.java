package com.backend.blooming.stamp.domain;

import com.backend.blooming.goal.domain.GoalTerm;
import com.backend.blooming.stamp.domain.exception.InvalidStampException;
import com.backend.blooming.common.util.DayUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class Day {

    @Column(name = "stamp_day", nullable = false)
    private long value;

    public Day(final GoalTerm goalTerm, final long day) {
        this.value = validateDay(goalTerm, day);
    }

    private long validateDay(final GoalTerm goalTerm, final long day) {
        final long nowStampDay = DayUtil.getNowDay(goalTerm.getStartDate());

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
