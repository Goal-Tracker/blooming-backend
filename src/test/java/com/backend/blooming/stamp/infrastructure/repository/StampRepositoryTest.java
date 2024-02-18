package com.backend.blooming.stamp.infrastructure.repository;

import com.backend.blooming.configuration.JpaConfiguration;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StampRepositoryTest extends StampRepositoryTestFixture {

    @Autowired
    private StampRepository stampRepository;

    @Test
    void 요청한_사용자_아이디와_날짜에_해당하는_스탬프가_존재하면_true를_반환한다() {
        // when
        final boolean result = stampRepository.existsByUserIdAndDayAndDeletedIsFalse(골_관리자_사용자.getId(), 1);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 요청한_사용자_아이디와_날짜에_해당하는_스탬프가_존재하지_않으면_false를_반환한다() {
        // when
        final boolean result = stampRepository.existsByUserIdAndDayAndDeletedIsFalse(골_관리자_사용자.getId(), 2);

        // then
        assertThat(result).isFalse();
    }
}
