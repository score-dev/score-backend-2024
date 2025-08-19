package com.score.backend.domain.user.repositories;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.domain.exercise.QExercise;
import com.score.backend.domain.friend.Friend;
import com.score.backend.domain.friend.QFriend;
import com.score.backend.domain.group.QGroupEntity;
import com.score.backend.domain.group.QUserGroup;
import com.score.backend.domain.user.QUser;
import com.score.backend.domain.user.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
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
        LocalDate today = LocalDate.now();

        return queryFactory
                .select(user)
                .from(userGroup)
                .join(userGroup.member, user)
                .join(userGroup.group, group) // 유저와 그룹을 inner join (어느 한 그룹에라도 속해 있는 유저만을 필터링)
                .leftJoin(user.feeds, exercise) // 유저와 운동 기록 left join (운동 기록이 존재하지 않는 유저도 포함되도록)
                .where(group.groupId.eq(groupId)) // 주어진 특정 그룹에 속한 유저만을 필터링
                .groupBy(user.id) // 모든 운동 기록들을 유저별로 그룹화
                // 그룹화된 유저별 운동 기록에 대해 집계 조건 적용
                .having(Expressions.numberTemplate(
                        Long.class, // 반환 값 타입
                        // case when A and B then 1 else 0 end: A와 B를 모두 만족하면 1 반환, 어느 하나라도 거짓이면 0 반환.
                        // sum(): 반환 값들의 합계 계산. 즉 조건을 만족시키는 운동 기록이 조회될 때마다 1씩 증가됨.
                        "sum(case when date({0}) = {1} " + // 조건 A: 운동을 끝낸 시각이 오늘의 시작 시각과 끝 시각 사이에 존재하는 경우 (= 오늘 이뤄진 운동인 경우)
                                "and timestampdiff(SECOND, {2}, {0}) >= 180 then 1 else 0 end)", // 조건 B: 해당 운동의 시작 시각과 끝낸 시각간의 차(운동을 한 시간)이 3분 이상인 경우
                        exercise.completedAt, // {0}
                        today, // {1}
                        exercise.startedAt // {2}
                ).eq(0L)) // 조건을 만족하는 운동 기록이 한 개도 없는 유저인 경우만을 필터링.
                .fetch();
    }

    @Override
    public List<User> findGroupMatesWhoDidExerciseToday(Long groupId) {
        LocalDate today = LocalDate.now();

        return queryFactory
                .select(user)
                .from(userGroup)
                .join(userGroup.member, user)
                .join(userGroup.group, group) // 유저와 그룹을 inner join (어느 한 그룹에라도 속해 있는 유저만을 필터링)
                .leftJoin(user.feeds, exercise) // 유저와 운동 기록 left join (운동 기록이 존재하지 않는 유저도 포함되도록)
                .where(group.groupId.eq(groupId)) // 주어진 특정 그룹에 속한 유저만을 필터링
                .groupBy(user.id) // 모든 운동 기록들을 유저별로 그룹화
                // 그룹화된 유저별 운동 기록에 대해 집계 조건 적용
                .having(Expressions.numberTemplate(
                        Integer.class, // 반환 값 타입
                        // case when A and B then 1 else 0 end: A와 B를 모두 만족하면 1 반환, 어느 하나라도 거짓이면 0 반환.
                        // sum(): 반환 값들의 합계 계산. 즉 조건을 만족시키는 운동 기록이 조회될 때마다 1씩 증가됨.
                        "sum(case when date({0}) = {1} " + // 조건 A: 오늘 이뤄진 운동인 경우
                                "and timestampdiff(SECOND, {2}, {0}) >= 180 then 1 else 0 end)", // 조건 B: 해당 운동의 시작 시각과 끝낸 시각간의 차(운동을 한 시간)이 3분 이상인 경우
                        exercise.completedAt, // {0}
                        today, // {1}
                        exercise.startedAt // {2}
                ).gt(0)) // 조건을 만족하는 운동 기록이 하나라도 있는 유저인 경우만을 필터링.
                .fetch();
    }
}
