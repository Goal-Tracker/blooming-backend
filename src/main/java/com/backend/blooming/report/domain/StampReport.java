package com.backend.blooming.report.domain;

import com.backend.blooming.common.entity.BaseTimeEntity;
import com.backend.blooming.report.application.exception.InvalidStampReportException;
import com.backend.blooming.report.application.exception.ReportForbiddenException;
import com.backend.blooming.stamp.domain.Stamp;
import com.backend.blooming.user.domain.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = {"reporter", "stamp"})
public class StampReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stamp_report_reporter"))
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stamp_report_stamp"))
    private Stamp stamp;

    @Embedded
    private Content content;

    public StampReport(final User reporter, final Stamp stamp, final Content content) {
        validateReporter(reporter, stamp);

        this.reporter = reporter;
        this.stamp = stamp;
        this.content = content;
    }

    private void validateReporter(final User reporter, final Stamp stamp) {
        if (stamp.isWriter(reporter)) {
            throw new InvalidStampReportException.NotAllowedReportOwnStampException();
        }
        if (!stamp.getGoal().isTeam(reporter)) {
            throw new ReportForbiddenException.StampReportForbiddenException();
        }
    }
}
