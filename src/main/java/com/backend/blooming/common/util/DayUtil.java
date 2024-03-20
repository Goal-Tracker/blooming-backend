package com.backend.blooming.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DayUtil {

    private static final int COUNT_GOAL_DAYS = 1;

    public static int getDays(final LocalDate startDate, final LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate) + COUNT_GOAL_DAYS;
    }

    public static int getNowDay(final LocalDate startDate) {
        return (int) ChronoUnit.DAYS.between(startDate, LocalDate.now()) + COUNT_GOAL_DAYS;
    }
}