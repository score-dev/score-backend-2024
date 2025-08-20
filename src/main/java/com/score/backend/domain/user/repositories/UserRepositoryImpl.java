package com.score.backend.domain.user.repositories;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.domain.exercise.QExercise;
import com.score.backend.domain.friend.Friend;
import com.score.backend.domain.friend.QFriend;
import com.score.backend.domain.group.QGroupEntity;
import com.score.backend.domain.group.QUserGroup;
import com.score.backend.domain.user.QUser;
import com.score.backend.domain.user.User;
import com.score.backend.dtos.GroupMateTodaysExerciseDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QUser user = QUser.user;
    QFriend friend = new QFriend("friend");
    QExercise exercise = QExercise.exercise;
    QGroupEntity group = QGroupEntity.groupEntity;
    QUserGroup userGroup = QUserGroup.userGroup;

    @Override
    public List<Friend> findFriendsByNicknameContaining(Long userId, String nickname) {
        return queryFactory
                .selectFrom(friend)
                .where(friend.user.id.eq(userId)
                        .and(friend.friend.nickname.containsIgnoreCase(nickname)))
                .fetch();
    }

    // 그룹 내에서 오늘 3분 이상 운동하지 않은 유저들의 목록 조회
    @Override
    public List<User> findGroupMatesWhoDidNotExerciseToday(Long groupId) {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = startOfToday.plusDays(1).minusSeconds(1);

        return queryFactory
                .select(user)
                .from(userGroup)
                .join(userGroup.member, user)
                .join(userGroup.group, group)
                .leftJoin(user.feeds, exercise)
                .where(group.groupId.eq(groupId))
                .groupBy(user.id)
                .having(Expressions.numberTemplate(
                        Integer.class,
                        "sum(case when {0} >= {1} and {0} <= {2} " +
                                "and {3} >= 180 then 1 else 0 end)",
                        exercise.completedAt, // {0}
                        startOfToday, // {1}
                        endOfToday, // {2}
                        exercise.durationSec // {3}
                ).eq(0))
                .fetch();
    }

    @Override
    public List<User> findGroupMatesWhoDidExerciseToday(Long groupId) {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = startOfToday.plusDays(1).minusSeconds(1);

        return queryFactory
                .select(user)
                .from(userGroup)
                .join(userGroup.member, user)
                .join(userGroup.group, group)
                .leftJoin(user.feeds, exercise)
                .where(group.groupId.eq(groupId))
                .groupBy(user.id)
                .having(Expressions.numberTemplate(
                        Integer.class,
                        "sum(case when {0} >= {1} and {0} <= {2} " +
                                "and {3} >= 180 then 1 else 0 end)",
                        exercise.completedAt, // {0}
                        startOfToday, // {1}
                        endOfToday, // {2}
                        exercise.durationSec // {3}
                ).gt(0))
                .fetch();
    }

    @Override
    public List<GroupMateTodaysExerciseDto> findGroupMatesTodaysExercises(Long groupId) {
        return queryFactory
                .select(Projections.constructor(
                        GroupMateTodaysExerciseDto.class,
                        user.id,
                        user.nickname,
                        user.profileImg,
                        user.lastExerciseDateTime,
                        didExerciseOrNot()
                ))
                .from(userGroup)
                .join(userGroup.member, user)
                .join(userGroup.group, group)
                .where(group.groupId.eq(groupId))
                .fetch();
    }

    private BooleanExpression didExerciseOrNot() {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = startOfToday.plusDays(1).minusSeconds(1);
        return JPAExpressions
                .selectOne()
                .from(exercise)
                .where(
                        exercise.agent.eq(user)
                                .and(exercise.completedAt.goe(startOfToday)).and(exercise.completedAt.loe(endOfToday))
                                .and(exercise.durationSec.goe(180))
                )
                .exists();

    }
}
