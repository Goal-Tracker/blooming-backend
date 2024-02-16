package com.backend.blooming.goal.domain;

import java.time.LocalDate;

@SuppressWarnings("NonAsciiCharacters")
public class GoalTermTestFixture {

    protected GoalTerm 수정_전_골_기간 = new GoalTerm(LocalDate.now(), LocalDate.now().plusDays(40));
    protected LocalDate 수정_요청한_종료날짜 = LocalDate.now().plusDays(50);
    protected long 수정_후_골_기간 = 51L;
}
