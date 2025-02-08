package com.score.backend.domain.user.repositories;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.domain.exercise.QExercise;
import com.score.backend.domain.group.QGroupEntity;
import com.score.backend.domain.group.QUserGroup;
import com.score.backend.domain.user.QUser;
import com.score.backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QUser user = QUser.user;
    QUser friend = new QUser("friend");
    QExercise exercise = QExercise.exercise;
    QGroupEntity group = QGroupEntity.groupEntity;
    QUserGroup userGroup = QUserGroup.userGroup;

    @Override
    public Page<User> findFriendsPage(long userId, Pageable pageable) {
        JPAQuery<User> where = queryFactory
                .select(friend)
                .from(user)
                .leftJoin(user.friends, friend)
                .where(user.id.eq(userId));
        List<User> users = where
                .orderBy(user.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = where.stream().count();

        return new PageImpl<>(users, pageable, total);
    }

    @Override
    public List<User> findFriendsByNicknameContaining(Long userId, String nickname) {
        return queryFactory
                .selectFrom(friend)
                .where(user.id.eq(userId)
                        .and(friend.nickname.containsIgnoreCase(nickname)))
                .fetch();
    }

    // 그룹 내에서 오늘 3분 이상 운동하지 않은 유저들의 목록 조회
    @Override
    public List<User> findGroupMatesWhoDidNotExerciseToday(Long groupId) {
        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay(); // 오늘의 시작 시각(00:00:00)
        LocalDateTime endOfToday = startOfToday.plusDays(1).minusSeconds(1); // 오늘의 끝 시각(23:59:59)

        return queryFactory
                .select(user)
                .from(user)
                .join(userGroup.group, group) // 유저와 그룹을 inner join (어느 한 그룹에라도 속해 있는 유저만을 필터링)
                .leftJoin(user.feeds, exercise) // 유저와 운동 기록 left join (운동 기록이 존재하지 않는 유저도 포함되도록)
                .where(group.groupId.eq(groupId)) // 주어진 특정 그룹에 속한 유저만을 필터링
                .groupBy(user.id) // 모든 운동 기록들을 유저별로 그룹화
                // 그룹화된 유저별 운동 기록에 대해 집계 조건 적용
                .having(Expressions.numberTemplate(
                        Long.class, // 반환 값 타입
                        // case when A and B then 1 else 0 end: A와 B를 모두 만족하면 1 반환, 어느 하나라도 거짓이면 0 반환.
                        // sum(): 반환 값들의 합계 계산. 즉 조건을 만족시키는 운동 기록이 조회될 때마다 1씩 증가됨.
                        "sum(case when {0} between {1} and {2} " + // 조건 A: 운동을 끝낸 시각이 오늘의 시작 시각과 끝 시각 사이에 존재하는 경우 (= 오늘 이뤄진 운동인 경우)
                                "and timestampdiff(SECOND, {3}, {4}) >= 180 then 1 else 0 end)", // 조건 B: 해당 운동의 시작 시각과 끝낸 시각간의 차(운동을 한 시간)이 3분 이상인 경우
                        exercise.completedAt, // {0}
                        startOfToday, // {1}
                        endOfToday, // {2}
                        exercise.startedAt, // {3}
                        exercise.completedAt // {4}
                ).eq(0L)) // 조건을 만족하는 운동 기록이 한 개도 없는 유저인 경우만을 필터링.
                .fetch();
    }

    @Override
    public List<User> findGroupMatesWhoDidExerciseToday(Long groupId) {
        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay(); // 오늘의 시작 시각(00:00:00)
        LocalDateTime endOfToday = startOfToday.plusDays(1).minusSeconds(1); // 오늘의 끝 시각(23:59:59)

        return queryFactory
                .select(user)
                .from(user)
                .join(userGroup.group, group) // 유저와 그룹을 inner join (어느 한 그룹에라도 속해 있는 유저만을 필터링)
                .leftJoin(user.feeds, exercise) // 유저와 운동 기록 left join (운동 기록이 존재하지 않는 유저도 포함되도록)
                .where(group.groupId.eq(groupId)) // 주어진 특정 그룹에 속한 유저만을 필터링
                .groupBy(user.id) // 모든 운동 기록들을 유저별로 그룹화
                // 그룹화된 유저별 운동 기록에 대해 집계 조건 적용
                .having(Expressions.numberTemplate(
                        Integer.class, // 반환 값 타입
                        // case when A and B then 1 else 0 end: A와 B를 모두 만족하면 1 반환, 어느 하나라도 거짓이면 0 반환.
                        // sum(): 반환 값들의 합계 계산. 즉 조건을 만족시키는 운동 기록이 조회될 때마다 1씩 증가됨.
                        "sum(case when {0} between {1} and {2} " + // 조건 A: 운동을 끝낸 시각이 오늘의 시작 시각과 끝 시각 사이에 존재하는 경우 (= 오늘 이뤄진 운동인 경우)
                                "and timestampdiff(SECOND, {3}, {4}) >= 180 then 1 else 0 end)", // 조건 B: 해당 운동의 시작 시각과 끝낸 시각간의 차(운동을 한 시간)이 3분 이상인 경우
                        exercise.completedAt, // {0}
                        startOfToday, // {1}
                        endOfToday, // {2}
                        exercise.startedAt, // {3}
                        exercise.completedAt // {4}
                ).gt(0)) // 조건을 만족하는 운동 기록이 한 개도 없는 유저인 경우만을 필터링.
                .fetch();
    }
}
