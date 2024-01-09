package com.backend.blooming.goal.domain;

import java.time.LocalDate;

@SuppressWarnings("NonAsciiCharacters")
public class GoalTermTestFixture {

    protected LocalDate 예외가_발생하는_경우_시작날짜 = LocalDate.now();
    protected LocalDate 예외가_발생하는_경우_종료날짜 = LocalDate.now().minusDays(40);
}
