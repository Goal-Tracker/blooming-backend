package com.backend.blooming.goal.domain;

import java.time.LocalDate;

@SuppressWarnings("NonAsciiCharacters")
public class GoalTermTestFixture {

    protected GoalTerm 수정_전_골_기간 = new GoalTerm(LocalDate.now(), LocalDate.now().plusDays(40));
}
