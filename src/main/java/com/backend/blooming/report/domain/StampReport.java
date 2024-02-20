package com.backend.blooming.report.domain;

import com.backend.blooming.common.entity.BaseTimeEntity;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.stamp.domain.Stamp;
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
@ToString
@Table
public class StampReport extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ReportInformation reportInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stamp_report_stamp"))
    private Stamp stamp;

    public StampReport(final ReportInformation reportInformation, Stamp stamp) {
        this.reportInformation = reportInformation;
        this.stamp = stamp;
    }
}
