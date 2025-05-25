package com.score.backend.domain.exercise.repositories;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.domain.exercise.Exercise;
import com.score.backend.domain.exercise.QExercise;
import com.score.backend.domain.group.QUserGroup;
import com.score.backend.domain.user.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExerciseRepositoryImpl implements ExerciseRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QExercise e = new QExercise("e");
    QUser u = new QUser("u");
    QUserGroup ug = new QUserGroup("ug");

    @Override
    public List<Exercise> findUsersWeeklyExercises(Long userId, LocalDate today) {
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return queryFactory
                .selectFrom(e)
                .where(userIdEq(userId), e.completedAt.between(monday.atStartOfDay(), today.plusDays(1).atStartOfDay()))
                .fetch();
    }

    @Override
    public int countUsersValidateExerciseToday(Long userId, LocalDate today) {
        return Math.toIntExact(
                queryFactory
                        .select(e.countDistinct())
                        .from(e)
                        .where(
                                userIdEq(userId),
                                e.completedAt.between(today.atStartOfDay(), today.plusDays(1).atStartOfDay()),
                                Expressions.numberTemplate(
                                        Long.class, "TIMESTAMPDIFF(SECOND, {0}, {1})", e.startedAt, e.completedAt
                                ).goe(180)
                        )
                        .fetchFirst()
        );
    }

    @Override
    public List<Exercise> findUsersMonthlyExercises(Long userId, int year, int month) {
        return queryFactory
                .selectFrom(e)
                .where(userIdEq(userId), e.completedAt.year().eq(year), e.completedAt.month().eq(month))
                .fetch();
    }

    @Override
    public Page<Exercise> findExercisePageByUserId(Long userId, Pageable pageable) {
        JPAQuery<Exercise> where = queryFactory
                .selectFrom(e)
                .where(e.agent.id.eq(userId));
        List<Exercise> exercises = where
                .orderBy(e.completedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(e.countDistinct())
                .from(e)
                .join(e.agent, u)
                .where(e.agent.id.eq(userId))
                .fetchOne();
        if (total == null) {
            total = 0L;
        }

        return new PageImpl<>(exercises, pageable, total);
    }

    @Override
    public Page<Exercise> findExercisePageByGroupId(Long groupId, Pageable pageable) {
        JPAQuery<Exercise> where = queryFactory
                .selectFrom(e)
                .join(e.agent, u)
                .join(u.userGroups, ug)
                .where(ug.group.groupId.eq(groupId));
        List<Exercise> exercises = where
                .orderBy(e.completedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(e.countDistinct())
                .from(e)
                .join(e.agent, u)
                .join(u.userGroups, ug)
                .where(ug.group.groupId.eq(groupId))
                .fetchOne();
        if (total == null) {
            total = 0L;
        }

        return new PageImpl<>(exercises, pageable, total);
    }

    private BooleanExpression userIdEq(Long userIdCond) {
        return userIdCond != null ? e.agent.id.eq(userIdCond) : null;
    }
}
