package com.backend.blooming.user.domain;

import com.backend.blooming.user.domain.exception.MemberException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class EmailTest {

    @Test
    void 이메일을_생성한다() {
        // given
        final String emailValue = "test@email.com";

        // when
        final Email actual = new Email(emailValue);

        // then
        assertThat(actual.getValue()).isEqualTo(emailValue);
    }

    @ParameterizedTest(name = "빈 값: {0}")
    @NullAndEmptySource
    void 빈값으로_이메일을_생성할시_예외를_반환한다(final String invalidEmailValue) {
        // when & then
        assertThatThrownBy(() -> new Email(invalidEmailValue))
                .isInstanceOf(MemberException.NullOrEmptyEmailException.class);
    }

    @Test
    void 이메일의_최대_길이를_초과한_값으로_이메일을_생성할시_예외를_반환한다() {
        // given
        final String invalidEmailValue = "12345678901234567890123456789012345678901234567890@email.com";

        // when & then
        assertThatThrownBy(() -> new Email(invalidEmailValue))
                .isInstanceOf(MemberException.LongerThanMaximumEmailLengthException.class);
    }

    @ParameterizedTest(name = "형식에 어긋난 이메일: {0}")
    @ValueSource(strings = {"email.com", "test@email", "test@email.", "@email.com", "test@.com"})
    void 이메일_형식에_맞지_않는_값으로_이메일을_생성할시_예외를_반환한다(final String invalidEmailValue) {
        // when & then
        assertThatThrownBy(() -> new Email(invalidEmailValue))
                .isInstanceOf(MemberException.InvalidEmailFormatException.class);
    }
}
