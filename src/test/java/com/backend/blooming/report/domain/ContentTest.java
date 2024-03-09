package com.backend.blooming.report.domain;

import com.backend.blooming.report.domain.exception.NullOrEmptyContentException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ContentTest {

    @ParameterizedTest
    @NullAndEmptySource
    void 신고_내용_생성시_내용이_null_혹은_빈값이라면_예외가_발생한다(final String value) {
        // when & then
        assertThatThrownBy(() -> new Content(value))
                .isInstanceOf(NullOrEmptyContentException.class);
    }
}
