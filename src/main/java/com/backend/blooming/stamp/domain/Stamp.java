package com.backend.blooming.stamp.domain;

import com.backend.blooming.common.entity.BaseTimeEntity;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.stamp.domain.exception.InvalidStampException;
import com.backend.blooming.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = {"goal", "user"})
public class Stamp extends BaseTimeEntity {

    private static final int STAMP_DAY_MINIMUM = 1;
    private static final int STAMP_MESSAGE_MAXIMUM = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", foreignKey = @ForeignKey(name = "fk_stamp_goal"), nullable = false)
    private Goal goal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_stamp_user"), nullable = false)
    private User user;

    @Column(name = "stamp_day", nullable = false)
    private int day;

    @Column(columnDefinition = "text", nullable = false)
    private String message;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Builder
    private Stamp(
            final Goal goal,
            final User user,
            final int day,
            final String message
    ) {
        this.goal = goal;
        this.user = user;
        this.day = validateDay(goal, day);
        this.message = validateMessage(message);
        this.goal.getStamps().add(this);
    }

    private int validateDay(final Goal goal, final int day) {
        final long nowStampDay = ChronoUnit.DAYS.between(goal.getGoalTerm().getStartDate(), LocalDate.now()) + 1;

        if (day > nowStampDay) {
            throw new InvalidStampException.InvalidStampDayFuture();
        }
        if (day > goal.getGoalTerm().getDays()) {
            throw new InvalidStampException.InvalidStampDay();
        }
        if (goal.getGoalTerm().getStartDate().isAfter(LocalDate.now())) {
            throw new InvalidStampException.InvalidStampDay();
        }

        return day;
    }

    private String validateMessage(final String message) {
        if (message == null || message.isEmpty()) {
            throw new InvalidStampException.InvalidStampMessage();
        }
        if (message.length() > STAMP_MESSAGE_MAXIMUM) {
            throw new InvalidStampException.InvalidStampMessage();
        }

        return message;
    }
}
