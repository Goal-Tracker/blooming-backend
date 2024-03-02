package com.backend.blooming.utils.days;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DayUtilTest extends DayUtilTestFixture {

    @Test
    void 골_시작날짜와_종료날짜를_입력하면_골_날짜수를_반환한다() {
        // when
        final int result = DayUtil.getDays(골_시작날짜, 골_종료날짜);

        // then
        assertThat(result).isEqualTo(골_날짜수);
    }

    @Test
    void 골_시작날짜를_입력하면_현재_골_진행_날짜를_반환한다() {
        // when
        final int result = DayUtil.getNowDay(골_시작날짜);

        // then
        assertThat(result).isEqualTo(현재_골_진행_날짜수);
    }
}
