package com.backend.blooming.goal.domain;

import com.backend.blooming.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "goalTeam")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = "goal")
public class GoalTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_team_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    @Builder
    private GoalTeam(final User user, final Goal goal){
        this.user = user;
        this.goal = goal;
    }

    public GoalTeam createGoalTeam(User user){
        validateUserIsNotNull(user);
        final GoalTeam goalTeam = GoalTeam.builder()
                .user(user)
                .build();

        return goalTeam;
    }

    public void updateGoal(Goal goal){
        validateGoalIsNotNull(goal);
        this.goal = goal;
    }

    private void validateUserIsNotNull(User user){
        if (user==null){
            throw new IllegalArgumentException("사용자 정보가 없습니다.");
        }
    }

    private void validateGoalIsNotNull(Goal goal){
        if (goal==null) {
            throw new IllegalArgumentException("골 정보가 없습니다.");
        }
    }
}