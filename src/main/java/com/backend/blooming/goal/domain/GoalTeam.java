package com.backend.blooming.goal.domain;

import com.backend.blooming.common.entity.BaseTimeEntity;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = {"user", "goal"})
public class GoalTeam extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_goal_team_goal"), nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", foreignKey = @ForeignKey(name = "fk_goal_team_user"), nullable = false)
    private Goal goal;

    @Column(nullable = false)
    private boolean accepted = false;

    @Column(nullable = false)
    private boolean deleted = false;

    public GoalTeam(final User user, final Goal goal) {
        this.user = user;
        this.goal = goal;
        processDefaultManager(user, goal);
    }

    private void processDefaultManager(final User user, final Goal goal) {
        if (goal.getManagerId().equals(user.getId())) {
            updateAccepted();
        }
    }

    public void updateAccepted() {
        this.accepted = true;
    }
}
