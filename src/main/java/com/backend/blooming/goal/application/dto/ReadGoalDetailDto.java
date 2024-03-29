package com.backend.blooming.goal.application.dto;

import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.themecolor.domain.ThemeColor;

import java.time.LocalDate;
import java.util.List;

public record ReadGoalDetailDto(
        Long id,
        String name,
        String memo,
        LocalDate startDate,
        LocalDate endDate,
        long days,
        Long managerId,
        List<GoalTeamDto> teams
) {

    public static ReadGoalDetailDto of(final Goal goal, final List<Long> usersUploadedStamp) {
        final List<GoalTeamDto> teams = goal.getTeams()
                                            .stream()
                                            .map(goalTeam -> GoalTeamDto.of(goalTeam, usersUploadedStamp))
                                            .toList();

        return new ReadGoalDetailDto(
                goal.getId(),
                goal.getName(),
                goal.getMemo(),
                goal.getGoalTerm().getStartDate(),
                goal.getGoalTerm().getEndDate(),
                goal.getGoalTerm().getDays(),
                goal.getManagerId(),
                teams
        );
    }

    public record GoalTeamDto(
            Long id,
            String name,
            ThemeColor color,
            String statusMessage,
            boolean uploadedTodayStamp
    ) {

        public static GoalTeamDto of(final GoalTeam goalTeam, final List<Long> usersUploadedStamp) {
            final boolean uploadedTodayStamp = usersUploadedStamp.contains(goalTeam.getUser().getId());

            return new GoalTeamDto(
                    goalTeam.getUser().getId(),
                    goalTeam.getUser().getName(),
                    goalTeam.getUser().getColor(),
                    goalTeam.getUser().getStatusMessage(),
                    uploadedTodayStamp
            );
        }
    }
}
