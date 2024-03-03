package com.backend.blooming.report.infrastructure.repository;

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
class GoalReportRepositoryTest extends GoalReportRepositoryTestFixture {

    @Autowired
    private GoalReportRepository goalReportRepository;

    @Test
    void 동일한_골을_동일한_사람이_이미_신고한_기록이_있다면_참을_반환한다() {
        // when
        final boolean actual = goalReportRepository.existsByReporterIdAndGoalId(신고자_아이디, 이미_신고한_골_아이디);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 특정_사용자가_특정_골을_신고한_기록이_없다면_거짓을_반환한다() {
        // when
        final boolean actual = goalReportRepository.existsByReporterIdAndGoalId(신고자_아이디, 골_아이디);

        // then
        assertThat(actual).isFalse();
    }
}
