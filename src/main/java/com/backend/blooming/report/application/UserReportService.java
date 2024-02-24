package com.backend.blooming.report.application;

import com.backend.blooming.report.application.dto.CreateUserReportDto;
import com.backend.blooming.report.application.exception.AlreadyReportUserException;
import com.backend.blooming.report.application.exception.NotAllowedReportOwnUserException;
import com.backend.blooming.report.domain.Content;
import com.backend.blooming.report.domain.UserReport;
import com.backend.blooming.report.infrastructure.repository.UserReportRepository;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserReportService {

    private final UserRepository userRepository;
    private final UserReportRepository userReportRepository;

    public Long create(final CreateUserReportDto userReportDto) {
        validateReport(userReportDto.reporterId(), userReportDto.reporteeId());
        final User reporter = getUser(userReportDto.reporterId());
        final User reportee = getUser(userReportDto.reporteeId());
        final Content content = new Content(userReportDto.content());

        final UserReport userReport = new UserReport(reporter, reportee, content);

        return userReportRepository.save(userReport)
                                   .getId();
    }

    private void validateReport(final Long reporterId, final Long reporteeId) {
        // TODO: 2024-02-25 자신을 신고하지 못한다는 예외는 신고 객체가 생성될 때 수행되는 게 더 적절할까요?
        if (reporterId.equals(reporteeId)) {
            throw new NotAllowedReportOwnUserException();
        }
        if (userReportRepository.existsByReporterIdAndReporteeId(reporterId, reporteeId)) {
            throw new AlreadyReportUserException();
        }
    }

    private User getUser(final Long userId) {
        return userRepository.findByIdAndDeletedIsFalse(userId)
                             .orElseThrow(NotFoundUserException::new);
    }
}
