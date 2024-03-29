package com.backend.blooming.report.infrastructure.repository;

import com.backend.blooming.report.domain.StampReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StampReportRepository extends JpaRepository<StampReport, Long> {

    boolean existsByReporterIdAndStampId(final Long reporterId, final Long stampId);
}
