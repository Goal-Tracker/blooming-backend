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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString
public class Stamp extends BaseTimeEntity {
    
    private static final int STAMP_DAY_MINIMUM = 1;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", foreignKey = @ForeignKey(name = "fk_stamp_goal"), nullable = false)
    private Goal goal;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_stamp_user"), nullable = false)
    private User user;
    
    @Column(nullable = false)
    private int day;
    
    @Column(nullable = false)
    private String message;
    
    private Stamp(
            final Goal goal,
            final User user,
            final int day,
            final String message
    ) {
        this.goal = goal;
        this.user = user;
        this.day = validateDay((int) goal.getGoalTerm().getDays(), day);
        this.message = validateMessage(message);
    }
    
    private int validateDay(final int goalDays, final int day) {
        if (day < STAMP_DAY_MINIMUM || day > goalDays) {
            throw new InvalidStampException.InvalidStampDay();
        }
        
        return day;
    }
    
    private String validateMessage(final String message) {
        if (message.isEmpty() || message.length() > 50) {
            throw new InvalidStampException.InvalidStampMessage();
        }
        
        return message;
    }
}
