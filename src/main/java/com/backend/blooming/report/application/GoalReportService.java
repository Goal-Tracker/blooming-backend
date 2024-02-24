package com.backend.blooming.report.application;

import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.report.application.dto.CreateGoalReportDto;
import com.backend.blooming.report.application.exception.AlreadyReportGoalException;
import com.backend.blooming.report.application.exception.GoalReportForbiddenException;
import com.backend.blooming.report.application.exception.NotAllowedReportOwnGoalException;
import com.backend.blooming.report.domain.Content;
import com.backend.blooming.report.domain.GoalReport;
import com.backend.blooming.report.infrastructure.repository.GoalReportRepository;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GoalReportService {

    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final GoalReportRepository goalReportRepository;

    public Long create(final CreateGoalReportDto goalReportDto) {
        validateReport(goalReportDto.reporterId(), goalReportDto.goalId());
        final User reporter = getUser(goalReportDto.reporterId());
        final Goal goal = getGoal(goalReportDto.goalId());
        validateReporter(reporter, goal);
        final Content content = new Content(goalReportDto.content());

        final GoalReport goalReport = new GoalReport(reporter, goal, content);

        return goalReportRepository.save(goalReport)
                                   .getId();
    }

    private void validateReport(final Long reporterId, final Long goalId) {
        if (goalReportRepository.existsByReporterIdAndGoalId(reporterId, goalId)) {
            throw new AlreadyReportGoalException();
        }
    }

    private User getUser(final Long userId) {
        return userRepository.findByIdAndDeletedIsFalse(userId)
                             .orElseThrow(NotFoundUserException::new);
    }

    private Goal getGoal(final Long goalId) {
        return goalRepository.findByIdAndDeletedIsFalse(goalId)
                             .orElseThrow(NotFoundGoalException::new);
    }

    private void validateReporter(final User reporter, final Goal goal) {
        if (goal.isManager(reporter.getId())) {
            throw new NotAllowedReportOwnGoalException();
        }
        if (!goal.isTeam(reporter)) {
            throw new GoalReportForbiddenException();
        }
    }
}
