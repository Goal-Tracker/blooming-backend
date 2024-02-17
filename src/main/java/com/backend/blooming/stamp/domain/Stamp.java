package com.backend.blooming.stamp.domain;

import com.backend.blooming.common.entity.BaseTimeEntity;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.user.domain.User;
import jakarta.persistence.Column;
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
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = {"goal", "user"})
public class Stamp extends BaseTimeEntity {

    private static final int STAMP_DAY_MINIMUM = 1;
    private static final int STAMP_MESSAGE_MAXIMUM = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", foreignKey = @ForeignKey(name = "fk_stamp_goal"), nullable = false)
    private Goal goal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_stamp_user"), nullable = false)
    private User user;

    @Embedded
    private Day day;

    @Embedded
    private Message message;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Builder
    private Stamp(
            final Goal goal,
            final User user,
            final Day day,
            final Message message
    ) {
        this.goal = goal;
        this.user = user;
        this.day = day;
        this.message = message;
    }
}
