package com.backend.blooming.goal.domain;

import java.time.LocalDate;

@SuppressWarnings("NonAsciiCharacters")
public class GoalTestFixture {

    protected String 골_제목 = "골 제목";
    protected LocalDate 골_시작일 = LocalDate.now();
    protected LocalDate 골_종료일 = LocalDate.now().plusDays(40);
    protected Long 골_관리자_아이디 = 1L;
}
