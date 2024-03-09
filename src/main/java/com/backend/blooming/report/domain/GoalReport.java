package com.backend.blooming.report.domain;

import com.backend.blooming.common.entity.BaseTimeEntity;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.report.application.exception.InvalidGoalReportException;
import com.backend.blooming.report.application.exception.ReportForbiddenException;
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
@ToString(exclude = {"reporter", "goal"})
public class GoalReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false, foreignKey = @ForeignKey(name = "fk_goal_report_reporter"))
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false, foreignKey = @ForeignKey(name = "fk_goal_report_goal"))
    private Goal goal;

    @Embedded
    private Content content;

    public GoalReport(final User reporter, final Goal goal, final Content content) {
        validateReporter(reporter, goal);

        this.reporter = reporter;
        this.goal = goal;
        this.content = content;
    }

    private void validateReporter(final User reporter, final Goal goal) {
        if (goal.isManager(reporter.getId())) {
            throw new InvalidGoalReportException.NotAllowedReportOwnGoalException();
        }
        if (!goal.isTeam(reporter)) {
            throw new ReportForbiddenException.GoalReportForbiddenException();
        }
    }
}
