package com.score.backend.repositories.exercise;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.models.exercise.Exercise;
import com.score.backend.models.exercise.QExercise;
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
        List<Exercise> exercises = queryFactory
                .selectFrom(e)
                .where(e.agent.id.eq(userId))
                .orderBy(e.completedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(e)
                .where(e.agent.id.eq(userId))
                .stream().count();

        return new PageImpl<>(exercises, pageable, total);
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
