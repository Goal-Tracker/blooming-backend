package com.backend.blooming.goal.domain;

import com.backend.blooming.goal.application.exception.InvalidGoalException;
import com.backend.blooming.common.util.DayUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class GoalTerm {
    
    private static final int GOAL_DAYS_MINIMUM = 1;
    private static final int GOAL_DAYS_MAXIMUM = 100;
    private static final int COUNT_GOAL_DAYS = 1;
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Column(nullable = false)
    private LocalDate endDate;
    
    @Column(nullable = false)
    private long days;
    
    public GoalTerm(final LocalDate startDate, final LocalDate endDate) {
        validateGoalDatePeriod(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = getValidGoalDays(startDate, endDate);
    }

    private void validateGoalDatePeriod(final LocalDate startDate, final LocalDate endDate) {
        final LocalDate nowDate = LocalDate.now();
        
        if (startDate.isBefore(nowDate)) {
            throw new InvalidGoalException.InvalidInvalidGoalStartDate();
        }
        if (endDate.isBefore(nowDate)) {
            throw new InvalidGoalException.InvalidInvalidGoalEndDate();
        }
        if (endDate.isBefore(startDate)) {
            throw new InvalidGoalException.InvalidInvalidGoalPeriod();
        }
    }

    private long getValidGoalDays(final LocalDate startDate, final LocalDate endDate) {
        final long goalDays = DayUtil.getDays(startDate, endDate);
        
        if (goalDays < GOAL_DAYS_MINIMUM || goalDays > GOAL_DAYS_MAXIMUM) {
            throw new InvalidGoalException.InvalidInvalidGoalDays();
        }
        
        return goalDays;
    }
    
    public void updateEndDate(final LocalDate endDate) {
        validateEndDate(endDate);
        this.endDate = endDate;
        this.days = getValidGoalDays(this.startDate, endDate);
    }
    
    private void validateEndDate(final LocalDate updateDate) {
        if (updateDate.isBefore(this.endDate)) {
            throw new InvalidGoalException.InvalidInvalidUpdateEndDate();
        }
    }
}
