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

    // TODO: 2024-02-20 해당 클래스를 세 곳에서 사용하게 되면, 중복 생성되는 fk 이름의 문제로 예외가 발생합니다.
    //  이떄, fk name을 포기하는 게 좋을 지, ReportInformation 필드들을 각각의 엔티티 클래스에 추가하여 각각의 다른 fk name을 설정하는 게 좋을지 잘 모르겠습니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportee_id", nullable = false)
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
