package com.backend.blooming.goal.domain;

import com.backend.blooming.goal.application.dto.CreateGoalTeamDto;
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

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString
public class GoalTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_team_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id")
    private Goal goal;

    @Builder
    private GoalTeam(final User user, final Goal goal){
        this.user = user;
        this.goal = goal;
    }

    public GoalTeam createGoalTeam(User user, Goal goal){
        validateUserIsNotNull(user);
        validateGoalIsNotNull(goal);
        final GoalTeam goalTeam = GoalTeam.builder()
                .user(user)
                .goal(goal)
                .build();
        return goalTeam;
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
