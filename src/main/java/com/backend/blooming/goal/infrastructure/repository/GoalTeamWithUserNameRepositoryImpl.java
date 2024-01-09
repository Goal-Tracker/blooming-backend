package com.backend.blooming.goal.infrastructure.repository;

import com.backend.blooming.goal.infrastructure.repository.dto.GoalTeamWithUserNameDto;
import com.backend.blooming.goal.infrastructure.repository.dto.GoalTeamWithUserQueryProjectionDto;
import com.backend.blooming.goal.infrastructure.repository.dto.QGoalTeamWithUserQueryProjectionDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.backend.blooming.goal.domain.QGoal.goal;
import static com.backend.blooming.goal.domain.QGoalTeam.goalTeam;
import static com.backend.blooming.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class GoalTeamWithUserNameRepositoryImpl implements GoalTeamWithUserNameRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GoalTeamWithUserNameDto> findAllByGoalIdAndDeletedIsFalse(final Long goalId) {
        final List<GoalTeamWithUserQueryProjectionDto> goalTeamWithUserQueryProjectionDtos =
                queryFactory.select(new QGoalTeamWithUserQueryProjectionDto(goalTeam, goal, user))
                            .from(goalTeam)
                            .join(goalTeam.goal)
                            .fetchJoin()
                            .join(goalTeam.user)
                            .fetchJoin()
                            .where(goalTeam.goal.id.eq(goalId)
                                                   .and(goalTeam.user.deleted.isFalse())
                                                   .and(goalTeam.deleted.isFalse()))
                            .fetch();

        return goalTeamWithUserQueryProjectionDtos.stream()
                                                  .map(dto -> new GoalTeamWithUserNameDto(dto.user().getId(),
                                                          dto.user().getName(),
                                                          dto.user().getColor()))
                                                  .collect(Collectors.toList());
    }
}
