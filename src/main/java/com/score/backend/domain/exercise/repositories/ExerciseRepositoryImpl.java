package com.score.backend.domain.exercise.repositories;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.domain.exercise.Exercise;
import com.score.backend.domain.exercise.QExercise;
import com.score.backend.domain.group.QGroupEntity;
import com.score.backend.domain.user.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
public class ExerciseRepositoryImpl implements ExerciseRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QExercise e = new QExercise("e");
    QUser u = new QUser("u");
    QGroupEntity g = new QGroupEntity("g");

    @Override
    public List<Exercise> findUsersExerciseToday(Long userId, LocalDateTime today) {
        return queryFactory
                .selectFrom(e)
                .where(userIdEq(userId), completeDateEq(today.truncatedTo(ChronoUnit.DAYS)))
                .fetch();
    }

    @Override
    public List<Exercise> findByUserId(Long userId) {
        return queryFactory
                .selectFrom(e)
                .where(userIdEq(userId))
                .fetch();
    }

    @Override
    public Page<Exercise> findExercisePageByUserId(Long userId, Pageable pageable) {
        JPAQuery<Exercise> where = queryFactory
                .selectFrom(e)
                .where();
        List<Exercise> exercises = where
                .orderBy(e.completedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = where.stream().count();

        return new PageImpl<>(exercises, pageable, total);
    }

    @Override
    public Page<Exercise> findExercisePageByGroupId(Long groupId, Pageable pageable) {

        JPAQuery<Exercise> where = queryFactory
                .selectFrom(e)
                .join(e.agent, u)
                .join(u.groups, g)
                .where(g.groupId.eq(groupId));
        List<Exercise> exercises = where
                .orderBy(e.completedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = where.stream().count();

        return new PageImpl<>(exercises, pageable, total);
    }

    @Override
    public Page<String> findFeedsImgPageByGroupId(Long groupId, Pageable pageable) {
        JPAQuery<String> where = queryFactory
                .select(e.exercisePic)
                .from(e)
                .join(u.groups, g)
                .where(g.groupId.eq(groupId));
        List<String> images = where
                .orderBy(e.completedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = where.stream().count();
        return new PageImpl<>(images, pageable, total);
    }

    private BooleanExpression userIdEq(Long userIdCond) {
        return userIdCond != null ? e.agent.id.eq(userIdCond) : null;
    }

    private BooleanExpression completeDateEq(LocalDateTime dateCond) {
        String formattedTime = dateCond.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", e.completedAt)
                .eq(formattedTime);
    }
}
