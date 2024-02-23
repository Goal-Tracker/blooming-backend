package com.backend.blooming.report.application;

import com.backend.blooming.report.application.dto.CreateStampReportDto;
import com.backend.blooming.report.application.exception.AlreadyReportStampException;
import com.backend.blooming.report.application.exception.NotAllowedReporterOwnStampException;
import com.backend.blooming.report.application.exception.StampReportForbiddenException;
import com.backend.blooming.report.domain.Content;
import com.backend.blooming.report.domain.StampReport;
import com.backend.blooming.report.infrastructure.repository.StampReportRepository;
import com.backend.blooming.stamp.application.exception.NotFoundStampException;
import com.backend.blooming.stamp.domain.Stamp;
import com.backend.blooming.stamp.infrastructure.repository.StampRepository;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StampReportService {

    private final StampReportRepository stampReportRepository;
    private final UserRepository userRepository;
    private final StampRepository stampRepository;

    public Long create(final CreateStampReportDto stampReportDto) {
        validateReport(stampReportDto.reporterId(), stampReportDto.stampId());
        final User reporter = getUser(stampReportDto.reporterId());
        final Stamp stamp = getStamp(stampReportDto.stampId());
        validateReporter(reporter, stamp);

        final Content content = new Content(stampReportDto.content());
        final StampReport stampReport = new StampReport(reporter, stamp, content);

        return stampReportRepository.save(stampReport)
                                    .getId();
    }

    private void validateReport(final Long userId, final Long stampId) {
        if (stampReportRepository.existsByReporterIdAndStampId(userId, stampId)) {
            throw new AlreadyReportStampException();
        }
    }

    private User getUser(final Long userId) {
        return userRepository.findByIdAndDeletedIsFalse(userId)
                             .orElseThrow(NotFoundUserException::new);
    }

    private Stamp getStamp(final Long stampId) {
        return stampRepository.findByIdAndFetchGoalAndUser(stampId)
                              .orElseThrow(NotFoundStampException::new);
    }

    private void validateReporter(final User reporter, final Stamp stamp) {
        if (stamp.isWriter(reporter)) {
            throw new NotAllowedReporterOwnStampException();
        }
        if (!stamp.getGoal().isTeam(reporter)) {
            throw new StampReportForbiddenException();
        }
    }
}
