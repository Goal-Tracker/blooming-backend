package com.backend.blooming.goal.infrastructure.repository;

import com.backend.blooming.goal.infrastructure.repository.dto.GoalTeamWithUserNameDto;
import com.backend.blooming.goal.infrastructure.repository.dto.QGoalTeamWithUserNameDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.backend.blooming.goal.domain.QGoal.goal;
import static com.backend.blooming.goal.domain.QGoalTeam.goalTeam;
import static com.backend.blooming.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class GoalTeamWithUserNameRepositoryImpl implements GoalTeamWithUserNameRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GoalTeamWithUserNameDto> findAllByGoalIdAndDeletedIsFalse(final Long goalId) {
        final List<GoalTeamWithUserNameDto> goalTeamWithUserNameDtos = queryFactory.select(new QGoalTeamWithUserNameDto(user.id, user.name, user.color))
                                                                                   .from(goalTeam)
                                                                                   .leftJoin(goalTeam.goal, goal)
                                                                                   .on(goalTeam.goal.id.eq(goalId))
                                                                                   .leftJoin(goalTeam.user, user)
                                                                                   .where(goalTeam.user.deleted.isFalse()
                                                                                                               .and(goalTeam.deleted.isFalse()))
                                                                                   .fetch();

        return goalTeamWithUserNameDtos;
    }
}
