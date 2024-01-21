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
        List<GoalTeamWithUserInfoDto> GoalTeamWithUserInfo
) {

    public static ReadGoalDetailDto from(final Goal goal) {
        final List<GoalTeamWithUserInfoDto> goalTeamWithUserInfoDtos = goal.getTeams()
                                                                           .stream()
                                                                           .map(GoalTeamWithUserInfoDto::from)
                                                                           .toList();

        return new ReadGoalDetailDto(
                goal.getId(),
                goal.getName(),
                goal.getMemo(),
                goal.getGoalTerm().getStartDate(),
                goal.getGoalTerm().getEndDate(),
                goal.getGoalTerm().getDays(),
                goal.getManagerId(),
                goalTeamWithUserInfoDtos
        );
    }

    public record GoalTeamWithUserInfoDto(Long id, String name, ThemeColor color, String statusMessage) {

        public static GoalTeamWithUserInfoDto from(final GoalTeam goalTeam) {
            return new GoalTeamWithUserInfoDto(
                    goalTeam.getId(),
                    goalTeam.getUser().getName(),
                    goalTeam.getUser().getColor(),
                    goalTeam.getUser().getStatusMessage()
            );
        }
    }
}
