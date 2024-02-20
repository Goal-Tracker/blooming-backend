package com.backend.blooming.report.domain;

import com.backend.blooming.report.domain.exception.InvalidReportException;
import com.backend.blooming.user.domain.User;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString(exclude = {"reporter", "reportee"})
public class ReportInformation {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false, foreignKey = @ForeignKey(name = "fk_report_reporter"))
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportee_id", nullable = false, foreignKey = @ForeignKey(name = "fk_report_reportee"))
    private User reportee;

    @Embedded
    private Content content;

    public ReportInformation(final User reporter, final User reportee, final Content content) {
        validateReportee(reporter, reportee);

        this.reporter = reporter;
        this.reportee = reportee;
        this.content = content;
    }

    private void validateReportee(final User reporter, final User reportee) {
        if (reporter.equals(reportee)) {
            throw new InvalidReportException.SelfReportingNotAllowedException();
        }
    }
}
