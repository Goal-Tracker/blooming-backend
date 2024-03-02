package com.backend.blooming.utils.days;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DayUtilTestFixture {

    protected final LocalDate 골_시작날짜 = LocalDate.now().minusDays(10);
    protected final LocalDate 골_종료날짜 = LocalDate.now().plusDays(10);
    protected final int 골_날짜수 = (int) ChronoUnit.DAYS.between(골_시작날짜, 골_종료날짜) + 1;
    protected final int 현재_골_진행_날짜수 = (int) ChronoUnit.DAYS.between(골_시작날짜, LocalDate.now()) + 1;
}
