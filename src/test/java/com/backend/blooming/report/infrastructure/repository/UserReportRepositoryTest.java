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
class UserReportRepositoryTest extends UserReportRepositoryTestFixture {

    @Autowired
    private UserReportRepository userReportRepository;

    @Test
    void 해당_사용자를_이미_신고했다면_참을_반환한다() {
        // when
        final boolean actual = userReportRepository.existsByReporterIdAndReporteeId(신고자_아이디, 이미_신고한_신고_대상자_아이디);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 해당_사용자를_아직_신고하지_않았다면_거짓을_반환한다() {
        // when
        final boolean actual = userReportRepository.existsByReporterIdAndReporteeId(신고자_아이디, 신고하지_않은_사람_아이디);

        // then
        assertThat(actual).isFalse();
    }
}
