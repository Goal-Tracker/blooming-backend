package com.backend.blooming.report.application;

import com.backend.blooming.report.application.dto.CreateUserReportDto;
import com.backend.blooming.report.domain.Content;
import com.backend.blooming.report.domain.ReportInformation;
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
        final User reporter = getUser(userReportDto.reporterId());
        final User reportee = getUser(userReportDto.reporteeId());

        // TODO: 2024-02-20 content와 ReportInformatin을 각각 생성하지 않고 그냥 신고 도메인 내에서 생성하는 것이 더 나을까요?
        final Content content = new Content(userReportDto.content());
        final ReportInformation reportInformation = new ReportInformation(reporter, reportee, content);
        final UserReport userReport = new UserReport(reportInformation);

        return userReportRepository.save(userReport)
                                   .getId();
    }

    private User getUser(final Long userId) {
        return userRepository.findByIdAndDeletedIsFalse(userId)
                             .orElseThrow(NotFoundUserException::new);
    }
}
