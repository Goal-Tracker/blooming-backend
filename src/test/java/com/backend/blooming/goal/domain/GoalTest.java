package com.backend.blooming.goal.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class GoalTest extends GoalTestFixture {

    @Test
    void 골_메모를_빈_값_또는_null로_받은_경우_비어있는_값으로_저장한다() {
        // when
        final Goal goal = Goal.builder()
                              .name(골_제목)
                              .memo("")
                              .startDate(골_시작일)
                              .endDate(골_종료일)
                              .managerId(골_관리자_아이디)
                              .users(골_참여자_목록)
                              .build();

        // then
        assertThat(goal.getMemo()).isEqualTo("");
    }

    @Test
    void 골_생성시_골_팀을_생성한다() {
        // when
        final Goal goal = Goal.builder()
                              .name(골_제목)
                              .memo("골 메모")
                              .startDate(골_시작일)
                              .endDate(골_종료일)
                              .managerId(골_관리자_아이디)
                              .users(골_참여자_목록)
                              .build();

        // then
        assertThat(goal.getTeams()).hasSize(2);
    }
}
