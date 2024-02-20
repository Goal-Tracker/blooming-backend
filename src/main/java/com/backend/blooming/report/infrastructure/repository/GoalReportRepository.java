package com.backend.blooming.report.infrastructure.repository;

import com.backend.blooming.report.domain.GoalReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalReportRepository extends JpaRepository<GoalReport, Long> {
}
