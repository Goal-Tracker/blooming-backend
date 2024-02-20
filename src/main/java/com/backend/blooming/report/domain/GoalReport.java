package com.backend.blooming.report.domain;

import com.backend.blooming.common.entity.BaseTimeEntity;
import com.backend.blooming.goal.domain.Goal;
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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = {"goal"})
@Table
public class GoalReport extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ReportInformation reportInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false, foreignKey = @ForeignKey(name = "fk_goal_report_goal"))
    private Goal goal;

    public GoalReport(final ReportInformation reportInformation, Goal goal) {
        this.reportInformation = reportInformation;
        this.goal = goal;
    }
}
