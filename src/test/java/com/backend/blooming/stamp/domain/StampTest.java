package com.backend.blooming.stamp.domain;

import com.backend.blooming.stamp.domain.exception.InvalidStampException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StampTest extends StampTestFixture {

    @Test
    void 스탬프를_생성한다() {
        // when
        final Stamp result = Stamp.builder()
                                  .goal(유효한_골)
                                  .user(유효한_사용자)
                                  .day(new Day(유효한_골.getGoalTerm(), 1))
                                  .message(new Message("테스트 메시지"))
                                  .stampImageUrl(스탬프_이미지_url)
                                  .build();

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).isNotNull();
            softAssertions.assertThat(result.getGoal().getName()).isEqualTo(유효한_골.getName());
            softAssertions.assertThat(result.getUser().getName()).isEqualTo(유효한_사용자.getName());
            softAssertions.assertThat(result.getDay()).isEqualTo(1);
            softAssertions.assertThat(result.getMessage()).isEqualTo("테스트 메시지");
            softAssertions.assertThat(result.getStampImageUrl()).isEqualTo(스탬프_이미지_url);
        });
    }

    @Test
    void 스탬프_생성시_이미지가_null이거나_빈값이면_빈_문자열로_주소를_저장한다() {
        // when
        final Stamp result = Stamp.builder()
                                  .goal(유효한_골)
                                  .user(유효한_사용자)
                                  .day(new Day(유효한_골.getGoalTerm(), 1))
                                  .message(new Message("테스트 메시지"))
                                  .build();

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).isNotNull();
            softAssertions.assertThat(result.getGoal().getName()).isEqualTo(유효한_골.getName());
            softAssertions.assertThat(result.getUser().getName()).isEqualTo(유효한_사용자.getName());
            softAssertions.assertThat(result.getDay()).isEqualTo(1);
            softAssertions.assertThat(result.getMessage()).isEqualTo("테스트 메시지");
            softAssertions.assertThat(result.getStampImageUrl()).isEqualTo("");
        });
    }

    @Test
    void 생성하려는_스탬프의_날짜가_골_시작일_이전인_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> Stamp.builder()
                                      .goal(스탬프_생성날짜가_골_시작일_보다_이전인_골)
                                      .user(유효한_사용자)
                                      .day(new Day(스탬프_생성날짜가_골_시작일_보다_이전인_골.getGoalTerm(), 스탬프_날짜가_골_시작일_이전인_경우))
                                      .message(new Message("스탬프 인증 메시지"))
                                      .build())
                .isInstanceOf(InvalidStampException.InvalidStampDay.class);
    }

    @Test
    void 생성하려는_스탬프가_오늘_이후일_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> Stamp.builder()
                                      .goal(유효한_골)
                                      .user(유효한_사용자)
                                      .day(new Day(유효한_골.getGoalTerm(), 스탬프_날짜가_현재_기준_스탬프_날짜보다_큰_경우))
                                      .message(new Message("스탬프 인증 메시지"))
                                      .build())
                .isInstanceOf(InvalidStampException.InvalidStampDayFuture.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 생성하려는_스탬프의_메시지가_비어있거나_50자_초과일_경우_예외를_발생한다(final String emptyMessage) {
        // when & then
        assertThatThrownBy(() -> Stamp.builder()
                                      .goal(유효한_골)
                                      .user(유효한_사용자)
                                      .day(new Day(유효한_골.getGoalTerm(), 1))
                                      .message(new Message(emptyMessage))
                                      .build())
                .isInstanceOf(InvalidStampException.InvalidStampMessage.class);
    }

    @Test
    void 스탬프_작성자라면_참을_반환한다() {
        // given
        final Stamp stamp = Stamp.builder()
                                 .goal(유효한_골)
                                 .user(유효한_사용자)
                                 .day(new Day(유효한_골.getGoalTerm(), 1))
                                 .message(new Message("테스트 메시지"))
                                 .build();

        // when
        final boolean actual = stamp.isWriter(유효한_사용자);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 스탬프_작성자가_아니라면_거짓을_반환한다() {
        // given
        final Stamp stamp = Stamp.builder()
                                 .goal(유효한_골)
                                 .user(유효한_사용자)
                                 .day(new Day(유효한_골.getGoalTerm(), 1))
                                 .message(new Message("테스트 메시지"))
                                 .build();

        // when
        final boolean actual = stamp.isWriter(작성자가_아닌_사용자);

        // then
        assertThat(actual).isFalse();
    }
}
