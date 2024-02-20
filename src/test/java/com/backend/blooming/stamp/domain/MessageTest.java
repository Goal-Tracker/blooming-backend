package com.backend.blooming.stamp.domain;

import com.backend.blooming.stamp.domain.exception.InvalidStampException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MessageTest {

    @ParameterizedTest
    @NullAndEmptySource
    void 생성하려는_스탬프의_메시지가_비어있거나_50자_초과일_경우_예외를_발생한다(final String emptyMessage) {
        // when & then
        assertThatThrownBy(() -> new Message(emptyMessage))
                .isInstanceOf(InvalidStampException.InvalidStampMessage.class);
    }
}
