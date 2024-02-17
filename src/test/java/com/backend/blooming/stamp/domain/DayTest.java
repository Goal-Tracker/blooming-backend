package com.backend.blooming.stamp.domain;

import com.backend.blooming.stamp.domain.exception.InvalidStampException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DayTest extends DayTestFixture {

    @Test
    void 생성하려는_스탬프의_날짜가_골_시작일_이전인_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> new Day(스탬프_생성날짜가_골_시작일_보다_이전인_골, 스탬프_날짜가_골_시작일_이전인_경우))
                .isInstanceOf(InvalidStampException.InvalidStampDay.class);
    }

    @Test
    void 생성하려는_스탬프가_오늘_이후일_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> new Day(유효한_골, 스탬프_날짜가_현재_기준_스탬프_날짜보다_큰_경우))
                .isInstanceOf(InvalidStampException.InvalidStampDayFuture.class);
    }

}
