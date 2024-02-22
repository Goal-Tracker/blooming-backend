package com.backend.blooming.report.infrastructure.repository;

import com.backend.blooming.report.domain.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    boolean existsByReporterIdAndReporteeId(final Long reporterId, final Long reporteeId);
}
