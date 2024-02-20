package com.backend.blooming.user.domain;

import com.backend.blooming.user.domain.exception.MemberException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NameTest {

    @Test
    void 이름을_생성한다() {
        // given
        final String nameValue = "name";

        // when
        final Name actual = new Name(nameValue);

        // then
        assertThat(actual.getValue()).isEqualTo(nameValue);
    }

    @ParameterizedTest(name = "빈 값: {0}")
    @NullAndEmptySource
    void 빈값으로_이름을_생성할시_예외를_반환한다(final String invalidNameValue) {
        // when & then
        assertThatThrownBy(() -> new Name(invalidNameValue))
                .isInstanceOf(MemberException.NullOrEmptyNameException.class);
    }

    @Test
    void 이름의_최대_길이를_초과한_값으로_이름을_생성할시_예외를_반환한다() {
        // given
        final String invalidNameValue = "12345678901234567890123456789012345678901234567890123456";

        // when & then
        assertThatThrownBy(() -> new Name(invalidNameValue))
                .isInstanceOf(MemberException.LongerThanMaximumNameLengthException.class);
    }

    @Test
    void 이름이_동일하다면_참을_반환한다() {
        // given
        final String nameValue = "name";
        final Name name1 = new Name(nameValue);
        final Name name2 = new Name(nameValue);

        // when
        final boolean actual = name1.isSame(name2);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 이름이_다르다면_거짓을_반환한다() {
        // given
        final Name name1 = new Name("name1");
        final Name name2 = new Name("name2");

        // when
        final boolean actual = name1.isSame(name2);

        // then
        assertThat(actual).isFalse();
    }
}
