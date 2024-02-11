package com.backend.blooming.stamp.infrastructure.repository;

import com.backend.blooming.configuration.JpaConfiguration;
import com.backend.blooming.stamp.domain.Stamp;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

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

    @Test
    void 요청한_골_아이디에_해당하는_모든_스탬프를_조회한다() {
        // when
        final List<Stamp> result = stampRepository.findAllByGoalIdAndDeletedIsFalse(유효한_골_아이디);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).hasSize(2);
            softAssertions.assertThat(result.get(0).getId()).isEqualTo(유효한_스탬프.getId());
            softAssertions.assertThat(result.get(1).getId()).isEqualTo(유효한_스탬프2.getId());
            softAssertions.assertThat(result.get(0).getDay()).isEqualTo(유효한_스탬프.getDay());
            softAssertions.assertThat(result.get(1).getDay()).isEqualTo(유효한_스탬프2.getDay());
        });
    }
}
